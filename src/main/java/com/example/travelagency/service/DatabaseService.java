package com.example.travelagency.service;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.dtos.TableDataResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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

    // Чтение SQL-запросов из файла
    public List<Map<String, String>> getSqlOptions() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("sql-options.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<List<Map<String, String>>>() {});
        }
    }

    // Получение столбцов таблицы
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

    // Получение строк таблицы
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

    // Выполнение произвольного SQL-запроса
    public TableDataResponse executeSqlQuery(String sql, String tableName) {
        // Заменяем table_name в запросе на актуальное имя таблицы
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

    // Получение информации о базе данных
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> response = new HashMap<>();

        // Имя базы данных из конфигурации
        response.put("dbName", databaseName);

        // SQL-запрос для получения списка таблиц
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ?";
        List<String> tables = jdbcTemplate.query(sql, new Object[]{databaseSchema}, (rs, rowNum) -> rs.getString("table_name"));

        response.put("tables", tables);
        return response;
    }
}