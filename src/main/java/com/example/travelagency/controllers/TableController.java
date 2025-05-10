package com.example.travelagency.controllers;

import com.example.travelagency.dtos.ColumnInfo;
import com.example.travelagency.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping("/{tableName}/columns")
    public List<ColumnInfo> getTableColumns(@PathVariable String tableName) throws SQLException {
        try {
            return tableService.getTableColumns(tableName);
        } catch (Exception e) {
            throw new SQLException("Ошибка при получении столбцов: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{tableName}/rows")
    public List<Map<String, Object>> getTableRows(@PathVariable String tableName) throws SQLException {
        try {
            return tableService.getTableRows(tableName);
        } catch (Exception e) {
            throw new SQLException("Ошибка при получении строк: " + e.getMessage(), e);
        }
    }

    @PostMapping("/{tableName}/rows")
    public ResponseEntity<Map<String, String>> addRow(@PathVariable String tableName, @RequestBody Map<String, Object> rowData) throws SQLException {
        try {
            tableService.addRow(tableName, rowData);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Строка успешно добавлена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SQLException("Ошибка при добавлении строки: " + e.getMessage(), e);
        }
    }

    @PutMapping("/{tableName}/{id}")
    public ResponseEntity<Map<String, String>> updateRow(@PathVariable String tableName, @PathVariable String id, @RequestBody Map<String, Object> rowData) throws SQLException {
        try {
            tableService.updateRow(tableName, id, rowData);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Строка успешно обновлена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SQLException("Ошибка при обновлении строки: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{tableName}/{id}")
    public ResponseEntity<Map<String, String>> deleteRow(@PathVariable String tableName, @PathVariable String id) throws SQLException {
        try {
            tableService.deleteRow(tableName, id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Строка успешно удалена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SQLException("Ошибка при удалении строки: " + e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createTable(@RequestBody Map<String, Object> request) throws SQLException {
        try {
            String tableName = (String) request.get("tableName");
            List<Map<String, Object>> columns = (List<Map<String, Object>>) request.get("columns");
            if (tableName == null || columns == null || tableName.isEmpty() || columns.isEmpty()) {
                throw new IllegalArgumentException("Table name and columns are required");
            }
            tableService.createTable(tableName, columns);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Таблица успешно создана");
            return ResponseEntity.ok(response); // Возвращаем JSON-объект
        } catch (Exception e) {
            throw new SQLException("Ошибка при создании таблицы: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{tableName}")
    public ResponseEntity<Map<String, String>> dropTable(@PathVariable String tableName) throws SQLException {
        try {
            if (tableName == null || tableName.isEmpty()) {
                throw new IllegalArgumentException("Table name is required");
            }
            tableService.dropTable(tableName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Таблица успешно удалена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new SQLException("Ошибка при удалении таблицы: " + e.getMessage(), e);
        }
    }
}