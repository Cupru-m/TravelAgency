package com.example.travelagency.service;

import com.example.travelagency.dtos.ColumnInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Map<String, Map<String, String>> typeCache = new HashMap<>();

    // Метод для получения типов столбцов таблицы
    private Map<String, String> getColumnTypes(String tableName) throws SQLException {
        return typeCache.computeIfAbsent(tableName.toLowerCase(), k -> {
            try {
                Map<String, String> types = new HashMap<>();
                DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
                try (ResultSet columns = metaData.getColumns(null, null, k, null)) {
                    while (columns.next()) {
                        String typeName = columns.getString("TYPE_NAME").toLowerCase();
                        // Преобразуем serial/bigserial в integer/bigint
                        if ("serial".equals(typeName)) {
                            typeName = "integer";
                        } else if ("bigserial".equals(typeName)) {
                            typeName = "bigint";
                        }
                        types.put(columns.getString("COLUMN_NAME").toLowerCase(), typeName);
                    }
                }
                return types;
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get column types for table " + tableName, e);
            }
        });
    }

    // Вспомогательный класс для хранения SQL и параметров
    private static class SqlQueryInfo {
        private final String sql;
        private final List<Object> params;

        public SqlQueryInfo(String sql, List<Object> params) {
            this.sql = sql;
            this.params = params;
        }

        public String getSql() {
            return sql;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    // Метод для формирования SQL с CAST
    private SqlQueryInfo buildSqlWithCasts(String tableName, String operation, Map<String, Object> data, String whereColumn, String whereType) {
        try {
            Map<String, String> columnTypes = getColumnTypes(tableName);
            String sql;
            List<Object> params = new ArrayList<>();

            switch (operation.toLowerCase()) {
                case "insert":
                    String columns = data.keySet().stream().collect(Collectors.joining(", "));
                    String placeholders = data.keySet().stream()
                            .map(key -> {
                                String type = columnTypes.getOrDefault(key.toLowerCase(), "varchar");
                                // Исключаем CAST для автоинкрементных полей при INSERT
                                return "serial".equals(type) || "bigserial".equals(type) ? "?" : "CAST(? AS " + type + ")";
                            })
                            .collect(Collectors.joining(", "));
                    sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
                    params.addAll(data.values().stream().map(v -> v != null ? v.toString() : null).collect(Collectors.toList()));
                    break;

                case "update":
                    String setClause = data.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals(whereColumn))
                            .map(entry -> entry.getKey() + " = CAST(? AS " + columnTypes.getOrDefault(entry.getKey().toLowerCase(), "varchar") + ")")
                            .collect(Collectors.joining(", "));
                    sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereColumn + " = CAST(? AS " + whereType + ")";
                    params.addAll(data.entrySet().stream()
                            .filter(entry -> !entry.getKey().equals(whereColumn))
                            .map(entry -> entry.getValue() != null ? entry.getValue().toString() : null)
                            .collect(Collectors.toList()));
                    params.add(data.get(whereColumn).toString());
                    break;

                case "delete":
                    sql = "DELETE FROM " + tableName + " WHERE " + whereColumn + " = CAST(? AS " + whereType + ")";
                    params.add(data.get(whereColumn).toString());
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported operation: " + operation);
            }

            return new SqlQueryInfo(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to build SQL query for table " + tableName + ": " + e.getMessage(), e);
        }
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

    public void addRow(String tableName, Map<String, Object> rowData) {
        if (rowData == null || rowData.isEmpty()) {
            throw new IllegalArgumentException("Row data cannot be null or empty");
        }

        SqlQueryInfo queryInfo = buildSqlWithCasts(tableName, "insert", rowData, null, null);
        try {
            jdbcTemplate.update(queryInfo.getSql(), queryInfo.getParams().toArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to add row to table " + tableName + ": " + e.getMessage(), e);
        }
    }

    public void updateRow(String tableName, String id, Map<String, Object> rowData) {
        if (rowData == null || rowData.isEmpty()) {
            throw new IllegalArgumentException("Row data cannot be null or empty");
        }

        Map<String, Object> filteredData = rowData.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("id"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (filteredData.isEmpty()) {
            throw new IllegalArgumentException("No data provided to update (only 'id' found in rowData)");
        }

        filteredData.put("id", id);
        SqlQueryInfo queryInfo = buildSqlWithCasts(tableName, "update", filteredData, "id", "integer");

        try {
            int rowsAffected = jdbcTemplate.update(queryInfo.getSql(), queryInfo.getParams().toArray());
            if (rowsAffected == 0) {
                throw new IllegalStateException("No rows updated. Possibly no record with id = " + id + " exists in " + tableName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update row in table " + tableName + " with id " + id + ": " + e.getMessage(), e);
        }
    }

    public void deleteRow(String tableName, String id) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        SqlQueryInfo queryInfo = buildSqlWithCasts(tableName, "delete", data, "id", "integer");

        try {
            int rowsAffected = jdbcTemplate.update(queryInfo.getSql(), queryInfo.getParams().toArray());
            if (rowsAffected == 0) {
                throw new IllegalStateException("No rows deleted. Possibly no record with id = " + id + " exists in " + tableName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete row from table " + tableName + " with id " + id + ": " + e.getMessage(), e);
        }
    }

    public void createTable(String tableName, List<Map<String, Object>> columns) {
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (int i = 0; i < columns.size(); i++) {
            Map<String, Object> column = columns.get(i);
            String name = (String) column.get("name");
            String type = (String) column.get("type");
            Boolean isPrimaryKey = (Boolean) column.get("isPrimaryKey");

            if (name == null || type == null || name.isEmpty() || type.isEmpty()) {
                throw new IllegalArgumentException("Column name and type are required");
            }

            sql.append(name).append(" ").append(type);
            if (isPrimaryKey != null && isPrimaryKey) {
                sql.append(" PRIMARY KEY");
            }
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        jdbcTemplate.execute(sql.toString());
    }

    public void dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName + " CASCADE";
        jdbcTemplate.execute(sql);
    }
}