package com.codesoom.assignment.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    @Test
    void isNumberFormatTest() {
        assertTrue(StringValidator.isNumberFormatValid("0"));
        assertTrue(StringValidator.isNumberFormatValid("6"));
        assertTrue(StringValidator.isNumberFormatValid("50"));

        assertFalse(StringValidator.isNumberFormatValid("05"));
        assertFalse(StringValidator.isNumberFormatValid("00"));

    }
}