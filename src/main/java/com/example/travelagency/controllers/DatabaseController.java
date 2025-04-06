package com.example.travelagency.controllers;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.dtos.SqlQueryRequest;
import com.example.travelagency.dtos.TableDataResponse;
import com.example.travelagency.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
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

    @GetMapping("/{tableName}/columns")
    public List<ColumnInfo> getTableColumns(@PathVariable String tableName) throws SQLException {
        try {
            return databaseService.getTableColumns(tableName);
        } catch (Exception e) {
            throw new SQLException("Ошибка при получении столбцов: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{tableName}/rows")
    public List<Map<String, Object>> getTableRows(@PathVariable String tableName) throws SQLException {
        try {
            return databaseService.getTableRows(tableName);
        } catch (Exception e) {
            throw new SQLException("Ошибка при получении строк: " + e.getMessage(), e);
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

    // Новый эндпоинт для выполнения SQL-запроса (только запрос)
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

    @GetMapping("/database-info")
    public Map<String, Object> getDatabaseInfo() {
        return databaseService.getDatabaseInfo();
    }
}