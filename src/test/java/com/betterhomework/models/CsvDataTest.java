package com.betterhomework.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvDataTest {

    private List<String> headers;
    private List<List<String>> rows;
    private CsvData csvData;

    @BeforeEach
    void setUp() {
        headers = new ArrayList<>(Arrays.asList("name", "subject", "duedate", "completed"));
        rows = new ArrayList<>();
        rows.add(new ArrayList<>(Arrays.asList("Math HW", "Math", "2024-12-25T10:00", "false")));
        rows.add(new ArrayList<>(Arrays.asList("Science HW", "Science", "2024-12-26T14:00", "true")));
        csvData = new CsvData(headers, rows);
    }

    @Test
    @DisplayName("Constructor sets headers and rows correctly")
    void testConstructor_ValidData() {
        assertEquals(4, csvData.headers().size());
        assertEquals(2, csvData.rows().size());
        assertEquals("name", csvData.headers().get(0));
        assertEquals("Math HW", csvData.rows().get(0).get(0));
    }

    @Test
    @DisplayName("Constructor accepts empty lists")
    void testConstructor_EmptyData() {
        CsvData emptyData = new CsvData(new ArrayList<>(), new ArrayList<>());
        assertTrue(emptyData.headers().isEmpty());
        assertTrue(emptyData.rows().isEmpty());
    }

    @Test
    @DisplayName("headers() returns correct headers")
    void testHeaders_Getter() {
        List<String> expectedHeaders = Arrays.asList("name", "subject", "duedate", "completed");
        assertEquals(expectedHeaders, csvData.headers());
    }

    @Test
    @DisplayName("setHeaders() updates headers correctly")
    void testHeaders_Setter() {
        List<String> newHeaders = Arrays.asList("col1", "col2");
        csvData.setHeaders(newHeaders);
        assertEquals(newHeaders, csvData.headers());
        assertEquals(2, csvData.headers().size());
    }

    @Test
    @DisplayName("rows() returns correct rows")
    void testRows_Getter() {
        assertEquals(2, csvData.rows().size());
        assertEquals("Math HW", csvData.rows().get(0).get(0));
        assertEquals("Science HW", csvData.rows().get(1).get(0));
    }

    @Test
    @DisplayName("setRows() updates rows correctly")
    void testRows_Setter() {
        List<List<String>> newRows = new ArrayList<>();
        newRows.add(Arrays.asList("New HW", "New Subject", "2024-12-30T12:00", "false"));
        csvData.setRows(newRows);
        assertEquals(1, csvData.rows().size());
        assertEquals("New HW", csvData.rows().get(0).get(0));
    }

    @Test
    @DisplayName("Returned list is mutable - modifications affect internal state")
    void testRows_Mutability() {
        List<List<String>> returnedRows = csvData.rows();
        returnedRows.add(Arrays.asList("Added HW", "Added Sub", "2024-12-27T08:00", "false"));

        // Verify that modifying the returned list affects the internal state
        assertEquals(3, csvData.rows().size());
    }
}
