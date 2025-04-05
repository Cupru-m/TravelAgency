package com.example.travelagency.controllers;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.dtos.SqlQueryRequest;
import com.example.travelagency.dtos.TableDataResponse;
import com.example.travelagency.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/execute-sql")
    public TableDataResponse executeSqlQuery(@RequestBody SqlQueryRequest request) throws SQLException {
        try {
            // Извлекаем tableName из запроса
            String tableName = request.getTableName();
            if (tableName == null || tableName.isEmpty()) {
                throw new IllegalArgumentException("Table name is required");
            }
            return databaseService.executeSqlQuery(request.getQuery(), tableName);
        } catch (Exception e) {
            throw new SQLException("Ошибка при выполнении SQL-запроса: " + e.getMessage(), e);
        }
    }

    @GetMapping("/database-info")
    public Map<String, Object> getDatabaseInfo() {
        return databaseService.getDatabaseInfo();
    }
}