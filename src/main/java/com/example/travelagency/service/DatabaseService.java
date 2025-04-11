package com.example.travelagency.service;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.dtos.SqlQueryRequest;
import com.example.travelagency.dtos.TableDataResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${database.name}")
    private String databaseName;

    @Value("${database.schema}")
    private String databaseSchema;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl; // Например, jdbc:postgresql://localhost:5432/travelagency

    private static final String BACKUP_DIR = "D:\\Studies\\3 term\\TravelAgency\\backups";
    private static final String BACKUP_FILE = BACKUP_DIR + "\\backup.dump";

    // Чтение SQL-запросов из файла
    public List<Map<String, String>> getSqlOptions() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("sql-options.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {});
        }
    }

    // Сохранение SQL-запроса в файл
    public void saveSqlQuery(SqlQueryRequest sqlQueryRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Resource resource = new ClassPathResource("sql-options.json");

        List<Map<String, String>> sqlOptions;
        try (InputStream inputStream = resource.getInputStream()) {
            sqlOptions = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {});
        }

        Map<String, String> newTemplate = new HashMap<>();
        newTemplate.put("name", sqlQueryRequest.getName());
        newTemplate.put("query", sqlQueryRequest.getQuery());
        sqlOptions.add(newTemplate);

        if (resource instanceof WritableResource) {
            try (OutputStream outputStream = ((WritableResource) resource).getOutputStream()) {
                objectMapper.writeValue(outputStream, sqlOptions);
            }
        } else {
            String filePath = "target/classes/sql-options.json";
            objectMapper.writeValue(Files.newOutputStream(Paths.get(filePath)), sqlOptions);
        }
    }

    public TableDataResponse executeSqlTemplate(String templateName, String tableName) throws IOException {
        List<Map<String, String>> sqlOptions = getSqlOptions();
        String sqlQuery = sqlOptions.stream()
                .filter(option -> option.get("name").equals(templateName))
                .findFirst()
                .map(option -> option.get("query"))
                .orElseThrow(() -> new IllegalArgumentException("Шаблон не найден: " + templateName));

        return executeSqlQuery(sqlQuery, tableName);
    }

    public List<ColumnInfo> getTableColumns(String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=0";
        return jdbcTemplate.query(sql, rs -> {
            List<ColumnInfo> columns = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columns.add(new ColumnInfo(metaData.getColumnName(i), metaData.getColumnTypeName(i)));
            }
            return columns;
        });
    }

    public List<Map<String, Object>> getTableRows(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            return row;
        });
    }

    public TableDataResponse executeSqlQuery(String sql, String tableName) {
        String finalSql = sql.replace("table_name", tableName);
        return jdbcTemplate.query(finalSql, rs -> {
            List<ColumnInfo> columns = new ArrayList<>();
            List<Map<String, Object>> rows = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(new ColumnInfo(metaData.getColumnName(i), metaData.getColumnTypeName(i)));
            }

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }

            return new TableDataResponse(columns, rows);
        });
    }

    public TableDataResponse executeSqlQuerySimple(String sql) {
        if (!sql.trim().toUpperCase().startsWith("SELECT")) {
            throw new IllegalArgumentException("Только SELECT-запросы разрешены");
        }
        return jdbcTemplate.query(sql, rs -> {
            List<ColumnInfo> columns = new ArrayList<>();
            List<Map<String, Object>> rows = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(new ColumnInfo(metaData.getColumnName(i), metaData.getColumnTypeName(i)));
            }

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                rows.add(row);
            }

            return new TableDataResponse(columns, rows);
        });
    }

    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("dbName", databaseName);
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ?";
        List<String> tables = jdbcTemplate.query(sql, new Object[]{databaseSchema}, (rs, rowNum) -> rs.getString("table_name"));
        response.put("tables", tables);
        return response;
    }

    public void backupDatabase() throws IOException {
        // Создаём директорию для бэкапов, если её нет
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            System.out.println("Создание директории для бэкапов: " + BACKUP_DIR);
            backupDir.mkdirs();
        }
        File backupFile = new File(BACKUP_FILE);
        if (!backupFile.exists()) {
            System.out.println("Создание файла бэкапа: " + BACKUP_FILE);
            backupFile.createNewFile();
        }

        // Извлекаем хост и порт из URL (jdbc:postgresql://localhost:5432/travelagency)
        String[] urlParts = dbUrl.split("://")[1].split("/");
        String hostPort = urlParts[0]; // localhost:5432
        String dbName = urlParts[1];   // travelagency

        // Формируем команду для pg_dump с полным путём
        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe", // Полный путь
                "--host", hostPort.split(":")[0], // localhost
                "--port", hostPort.split(":")[1], // 5432
                "--username", dbUsername,
                "--format", "custom", // Формат .dump для восстановления через pg_restore
                "--file", BACKUP_FILE,
                dbName
        );
        pb.environment().put("PGPASSWORD", dbPassword); // Устанавливаем пароль через переменную окружения
        pb.redirectErrorStream(true); // Перенаправляем ошибки в стандартный вывод

        System.out.println("Выполнение команды pg_dump: " + String.join(" ", pb.command()));
        System.out.println("Проверка файла pg_dump: " + new File("C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe").exists());
        System.out.println("Проверка файла бэкапа: " + backupFile.exists());

        try {
            Process process = pb.start();

            // Читаем вывод для диагностики
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    System.out.println("pg_dump output: " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("pg_dump завершился с кодом: " + exitCode);
            if (exitCode != 0) {
                throw new IOException("pg_dump завершился с ошибкой: " + exitCode + "\nВывод:\n" + output.toString());
            }
            System.out.println("Резервное копирование завершено успешно");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Процесс pg_dump был прерван", e);
        }
    }

    public void restoreDatabase() throws IOException {
        File backupFile = new File(BACKUP_FILE);
        if (!backupFile.exists()) {
            System.out.println("Файл резервной копии не найден: " + BACKUP_FILE);
            throw new IOException("Файл резервной копии не найден: " + BACKUP_FILE);
        }

        String[] urlParts = dbUrl.split("://")[1].split("/");
        String hostPort = urlParts[0]; // localhost:5432
        String dbName = urlParts[1];   // travel_agency

        // Очистка базы данных перед восстановлением
        try {
            System.out.println("Очистка базы данных " + dbName + " перед восстановлением");
            jdbcTemplate.execute("DO $$ " +
                    "DECLARE " +
                    "    r RECORD; " +
                    "BEGIN " +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP " +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE'; " +
                    "    END LOOP; " +
                    "END $$;");
            System.out.println("Все таблицы в схеме public удалены");
        } catch (Exception e) {
            System.out.println("Ошибка при очистке базы: " + e.getMessage());
            throw new IOException("Не удалось очистить базу перед восстановлением: " + e.getMessage(), e);
        }

        String dbConnectionString = "postgresql://" + dbUsername + ":" + dbPassword + "@" + hostPort + "/" + dbName;

        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_restore.exe",
                "--dbname", dbConnectionString,
                "--verbose",
                "D:\\Studies\\3 term\\TravelAgency\\backups\\backup.dump"
        );
        pb.redirectErrorStream(true);

        System.out.println("Выполнение команды pg_restore: " + String.join(" ", pb.command()));
        System.out.println("Проверка файла pg_restore: " + new File("C:\\Program Files\\PostgreSQL\\16\\bin\\pg_restore.exe").exists());
        System.out.println("Проверка файла бэкапа: " + backupFile.exists());

        try {
            Process process = pb.start();

            // Читаем вывод с явной кодировкой UTF-8
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    System.out.println("pg_restore output: " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("pg_restore завершился с кодом: " + exitCode);
            if (exitCode != 0) {
                throw new IOException("pg_restore завершился с ошибкой: " + exitCode + "\nВывод:\n" + output.toString());
            }
            System.out.println("Восстановление базы данных завершено успешно");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Процесс pg_restore был прерван", e);
        }
    }
}