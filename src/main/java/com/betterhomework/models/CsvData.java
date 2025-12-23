package com.betterhomework.models;

import java.util.List;

public class CsvData {
    List<String> headers;
    List<List<String>> rows;

    public CsvData(List<String> headers, List<List<String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public List<String> headers() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> rows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }
}