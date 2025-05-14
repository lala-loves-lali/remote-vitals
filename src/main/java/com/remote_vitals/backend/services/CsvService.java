package com.remote_vitals.backend.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class CsvService {

    /**
     * Parses a CSV file from the given absolute path.
     * Each row is converted into a Map with lowercase, trimmed column headers as keys.
     * Key-value pairs with empty or blank values are skipped.
     * Ensures headers include required columns but ignores extra columns.
     * If headers are invalid or a row contains a different number of comma-separated values
     * than the header count, an IllegalArgumentException is thrown.
     *
     * @param absolutePath Absolute path to the CSV file.
     * @return List of Maps, each representing a non-empty row of the CSV.
     * @throws IOException if the file cannot be read.
     * @throws IllegalArgumentException if headers are invalid or rows have inconsistent column counts.
     */
    public List<Map<String, String>> parseCsv(String absolutePath) throws IOException {
        List<Map<String, String>> resultList = new ArrayList<>();
        System.out.println("Parsing CSV file at: " + absolutePath);

        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            // Read the header line
            String headerLine = br.readLine();
            if (headerLine == null) {
                return resultList; // Empty file
            }

            // Remove BOM if present
            if (headerLine.startsWith("\uFEFF")) {
                headerLine = headerLine.substring(1);
            }

            // Normalize and trim headers
            String[] rawHeaders = headerLine.split(",", -1);
            String[] headers = Arrays.stream(rawHeaders)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toArray(String[]::new);

            System.out.println("Uploaded Headers (normalized): " + Arrays.toString(headers));

            // Validate headers
            if (!validateHeaders(headers)) {
                throw new IllegalArgumentException("CSV header validation failed: unexpected or missing columns.");
            }

            int expectedColumns = headers.length;
            String line;
            int lineNumber = 1; // Header is line 1

            // Process all rows
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] rawValues = line.split(",", -1);

                // Check for row length consistency
                if (rawValues.length != expectedColumns) {
                    throw new IllegalArgumentException(
                            "CSV format error at line " + lineNumber +
                                    ": expected " + expectedColumns + " values, but found " + rawValues.length + "."
                    );
                }

                // Map each row with headers as keys
                Map<String, String> rowMap = new HashMap<>();
                for (int i = 0; i < expectedColumns; i++) {
                    String key = headers[i];
                    String value = rawValues[i].trim();

                    if (!value.isEmpty()) {
                        rowMap.put(key, value);
                    }
                }

                if (!rowMap.isEmpty()) {
                    resultList.add(rowMap);
                }
            }
        }

        System.out.println("Parsed CSV data: " + resultList.size() + " records found.");
        return resultList;
    }

    /**
     * Validates that the required headers are present in the CSV file
     * and ignores extra columns.
     *
     * @param headers Array of headers to validate.
     * @return true if the required headers are valid, false otherwise.
     */
    private boolean validateHeaders(String[] headers) {
        // Required minimum headers
        List<String> requiredHeaders = Arrays.asList(
                "date-time",
                "body-temperature",
                "heart-rate"
        );

        // Normalize headers from the CSV
        List<String> uploadedHeaders = Arrays.stream(headers)
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        System.out.println("Required Headers: " + requiredHeaders);
        System.out.println("Uploaded Headers: " + uploadedHeaders);

        // Check for missing required headers
        for (String requiredHeader : requiredHeaders) {
            if (!uploadedHeaders.contains(requiredHeader)) {
                System.out.println("Missing required header: " + requiredHeader);
                return false;
            }
        }

        System.out.println("Header validation successful.");
        return true;
    }
}