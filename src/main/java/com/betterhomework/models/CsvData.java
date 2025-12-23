package com.betterhomework.models;

import java.util.List;

public record CsvData(List<String> headers, List<List<String>> rows) {}
