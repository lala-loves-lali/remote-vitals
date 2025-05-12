package com.remote_vitals.backend.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DateAndTimeService {

    /**
     * Parses the given date string using the specified format and returns a LocalDate object.
     * If parsing fails or inputs are invalid, null is returned.
     *
     * @param date   The date string to parse.
     * @param format The format to use for parsing.
     * @return LocalDate object if successful, otherwise null.
     */
    public LocalDate analyzeDateFormat(String date, String format) {
        try {
            if (date == null || format == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Parses the given date-time string using the specified format and returns a LocalDateTime object.
     * If parsing fails or inputs are invalid, null is returned.
     *
     * @param dateTime The date-time string to parse.
     * @param format   The format to use for parsing.
     * @return LocalDateTime object if successful, otherwise null.
     */
    public LocalDateTime analyzeDateTimeFormat(String dateTime, String format) {
        try {
            if (dateTime == null || format == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Validates whether the given date string strictly matches the specified format.
     * It checks for proper formatting and valid date values (e.g., no 32nd day of a month).
     *
     * @param date   The date string to validate.
     * @param format The format to use for validation.
     * @return true if the date matches the format and is valid, false otherwise.
     */
    public boolean matchDateformat(String date, String format) {
        if (date == null || format == null) return false;
        try {
            date = date.trim();  // Trim any extra spaces
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(date, formatter);  // Parse the date
            return true;  // Only reached if parsing succeeds
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }


    /**
     * Validates whether the given date-time string strictly matches the specified format.
     * It ensures both the date and time components are correctly formatted and valid.
     *
     * @param dateTime The date-time string to validate.
     * @param format   The format to use for validation.
     * @return true if the date-time matches the format and is valid, false otherwise.
     */
    public boolean matchDateTimeFormat(String dateTime, String format) {
        if (dateTime == null || format == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            LocalDateTime.parse(dateTime, formatter); // throws if invalid
            return true; // only reached if parsing succeeds
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }


}
