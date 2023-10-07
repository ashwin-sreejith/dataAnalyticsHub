package com.ashwin.dataanalyticshub.datamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// performs basic type validations for inputs
public class Util {

    // validates string inputs and returns true if valid
    public static boolean isValidString(String input) {
        // input is not null, empty or has commas
        return input != null && !input.trim().isEmpty() && !input.contains(",");
    }

    // validates integer inputs and returns true if valid
    public static boolean isValidInteger(String input, boolean acceptZero) {
        try {
            int entry = Integer.parseInt(input);
            // 0 is valid
            if (acceptZero)
                return entry >= 0;
            // 0 is invalid
            else
                return entry > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // validates DateTime inputs and returns true if valid
    public static boolean isValidDateTime(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
            LocalDateTime.parse(input, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // validates row of details from csv and returns true if valid
    public static boolean isValidLine(String input) {
        String[] values = input.split(",");
        // if no entry found
        if (values.length == 0){
            return false;
        }
        // if extra entries found
        if (values.length > 6) {
            return false;
        }
        for (String value : values) {
            // if value is empty
            if (value.isEmpty()) {
                return false;
            }
        }
        return isValidInteger(values[0], false) &&
                isValidString(values[1]) &&
                isValidString(values[2]) &&
                isValidInteger(values[3], true) &&
                isValidInteger(values[4], true) &&
                isValidDateTime(values[5]);
    }


    public static String dateFormatterFunc(LocalDateTime dateString) {

        // Parse the input date string
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(String.valueOf(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Format the parsed date to the desired SQL date format
        SimpleDateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sqlDateFormatter.format(parsedDate);

        System.out.println("Formatted date for SQL: " + formattedDate);
        return formattedDate;
    }

    public static LocalDateTime localDateTimeFormatFunc(String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(dateString, formatter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        

        return dateTime;
    }

}
