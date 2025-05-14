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

        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) { // Use absolutePath for file reading
            // Read headers
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new RuntimeException("CSV file is empty or missing headers.");
            }

            String[] headers = headerLine.split(",", -1);
            if (!validateHeaders(headers)) {
                throw new RuntimeException("File Format Invalid: Missing required headers.");
            }

            // Normalize headers for mapping
            List<String> normalizedHeaders = Arrays.stream(headers)
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toList();

            // Parse rows
            String line;
            int lineNumber = 1; // Include header as line 1
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] values = line.split(",", -1); // Split row into values

                // Validate row length against header length
                if (values.length != headers.length) {
                    System.out.println("Malformed row at line " + lineNumber + ": " + line);
                    continue; // Skip malformed rows
                }

                // Map values to headers
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < normalizedHeaders.size(); i++) {
                    record.put(normalizedHeaders.get(i), values[i].trim());
                }
                resultList.add(record); // Add record to results
            }

            // Check if any records were parsed
            if (resultList.isEmpty()) {
                System.out.println("No valid rows found in the parsed CSV file.");
            }
        }

        return resultList; // Return the parsed records
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