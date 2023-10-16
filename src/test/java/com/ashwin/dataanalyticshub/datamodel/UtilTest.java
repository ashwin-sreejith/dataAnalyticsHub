package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.datamodel.Util;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
class UtilTest {

    @Test
    void isValidStringEmpty() {
        assertFalse(Util.isValidString(""), "True returned when string is empty");
    }

    // tests if null string is rejected
    @Test
    void isValidStringNull() {
        assertFalse(Util.isValidString(null), "True returned when input is null");
    }

    // tests if string with comma is rejected
    @Test
    void isValidStringComma() {
        assertFalse(Util.isValidString("Hello,hi"), "True returned when string has comma");
    }

    // tests for valid integer
    @Test
    void isValidInteger() {
        assertTrue(Util.isValidInteger("5", false), "valid integer not accepted");
    }

    // tests if 0 is accepted
    @Test
    void isValidIntegerWithZero() {
        assertTrue(Util.isValidInteger("0", true), "0 not accepted");
    }

    // tests if 0 is rejected
    @Test
    void isValidIntegerWithoutZero() {
        assertFalse(Util.isValidInteger("0", false), "0 accepted");
    }

    // tests if negative integers are rejected
    @Test
    void isValidIntegerNegativeInvalid() {
        assertFalse(Util.isValidInteger("-5", false), "negative value accepted");
    }

    // tests if non integers are rejected
    @Test
    void isValidIntegerStringInvalid() {
        assertFalse(Util.isValidInteger("hello", false), "Invalid integer accepted");
    }

    // tests if floats or double are rejected
    @Test
    void isValidIntegerDecimalInvalid() {
        assertFalse(Util.isValidInteger("5.0", false), "Invalid integer accepted");
    }

    // tests if valid date time is accepted
    @Test
    void isValidDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
        String validDateTime = LocalDateTime.now().format(formatter);
        assertTrue(Util.isValidDateTime(validDateTime), "valid date not accepted");
    }

    // tests if invalid date time is rejected
    @Test
    void isValidDateTimeNonDateInvalid() {
        assertFalse(Util.isValidDateTime("44/13/2222 25:65"), "invalid date accepted");
    }

    // tests if valid lines are accepted
    @Test
    void isValidLine() {
        String validLine = "1,Content,Author,18,4,18/04/2023 03:00";
        assertTrue(Util.isValidLine(validLine), "valid line not accepted");
    }

    // tests if lines with empty entries are rejected
    @Test
    void isValidLineEmptyValue() {
        String validLine = "1,Content,Author,18,,4,18/04/2023 03:00";
        assertFalse(Util.isValidLine(validLine), "accepts empty entries in the line");
    }

    // tests if lines with extra details are rejected
    @Test
    void isValidLineExtraDetail() {
        String validLine = "1,Content,Author,18,22,4,18/04/2023 03:00";
        assertFalse(Util.isValidLine(validLine), "accepts extra details");
    }

}