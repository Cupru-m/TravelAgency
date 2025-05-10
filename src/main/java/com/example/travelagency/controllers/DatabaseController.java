package com.example.travelagency.controllers;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.dtos.SqlQueryRequest;
import com.example.travelagency.dtos.TableDataResponse;
import com.example.travelagency.service.DatabaseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/sql-options")
    public List<Map<String, String>> getSqlOptions() throws IOException {
        return databaseService.getSqlOptions();
    }

    @PostMapping("/sql-options")
    public ResponseEntity<String> saveSqlTemplate(@RequestBody SqlQueryRequest sqlQueryRequest) {
        try {
            databaseService.saveSqlQuery(sqlQueryRequest);
            return ResponseEntity.ok("SQL-шаблон успешно сохранён");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Ошибка при сохранении SQL-шаблона: " + e.getMessage());
        }
    }

    @PostMapping("/execute-sql-template")
    public ResponseEntity<TableDataResponse> executeSqlTemplate(@RequestBody Map<String, String> request) throws SQLException {
        try {
            String templateName = request.get("templateName");
            String tableName = request.get("tableName");
            if (templateName == null || tableName == null || templateName.isEmpty() || tableName.isEmpty()) {
                throw new IllegalArgumentException("Template name and table name are required");
            }
            TableDataResponse result = databaseService.executeSqlTemplate(templateName, tableName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new SQLException("Ошибка при выполнении SQL-шаблона: " + e.getMessage(), e);
        }
    }

    @PostMapping("/execute-sql-simple")
    public ResponseEntity<TableDataResponse> executeSqlQuerySimple(@RequestBody Map<String, String> request) throws SQLException {
        try {
            String sqlQuery = request.get("query");
            if (sqlQuery == null || sqlQuery.isEmpty()) {
                throw new IllegalArgumentException("SQL query is required");
            }
            TableDataResponse result = databaseService.executeSqlQuerySimple(sqlQuery);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new SQLException("Ошибка при выполнении SQL-запроса: " + e.getMessage(), e);
        }
    }

    @GetMapping("/info")
    public Map<String, Object> getDatabaseInfo() {
        return databaseService.getDatabaseInfo();
    }

    @PostMapping("/backup")
    public ResponseEntity<Map<String, String>> backupDatabase() throws IOException {
        try {
            databaseService.backupDatabase();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Резервная копия успешно создана");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new IOException("Ошибка при создании резервной копии: " + e.getMessage(), e);
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<Map<String, String>> restoreDatabase() throws IOException {
        try {
            databaseService.restoreDatabase();
            Map<String, String> response = new HashMap<>();
            response.put("message", "База данных успешно восстановлена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new IOException("Ошибка при восстановлении базы данных: " + e.getMessage(), e);
        }
    }
    @PostMapping("/export-to-excel")
    public ResponseEntity<org.springframework.core.io.Resource> exportToExcel(@RequestBody TableDataResponse tableData) throws IOException {
        // Создаём новый Excel-файл
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");

        // Заголовки
        Row headerRow = sheet.createRow(0);
        List<ColumnInfo> columns = tableData.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i).getName());
        }

        // Данные
        List<Map<String, Object>> rows = tableData.getRows();
        for (int i = 0; i < rows.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Map<String, Object> rowData = rows.get(i);
            for (int j = 0; j < columns.size(); j++) {
                String columnName = columns.get(j).getName();
                Object value = rowData.get(columnName);
                if (value != null) {
                    row.createCell(j).setCellValue(value.toString());
                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        // Записываем Excel в поток
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Формируем ресурс
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        // Настраиваем заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table_data.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(outputStream.size())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}