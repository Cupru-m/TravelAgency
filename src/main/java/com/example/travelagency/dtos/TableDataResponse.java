package com.example.travelagency.dtos;

import java.util.List;
import java.util.Map;

public class TableDataResponse {
    private List<ColumnInfo> columns;
    private List<Map<String, Object>> rows;

    public TableDataResponse(List<ColumnInfo> columns, List<Map<String, Object>> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}