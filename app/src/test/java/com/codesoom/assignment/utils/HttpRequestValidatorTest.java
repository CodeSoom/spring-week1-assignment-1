package com.codesoom.assignment.utils;

import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestValidatorTest {

    @Test
    @DisplayName("URI 유효성 테스트 - invalid path")
    void checksPathInvalidTest() {

        final String[] invalidPathArr = {null, "tasks", "1111", "/1111", "/tasks/exercise", "tasks/", "tasks/1", "/tasks/exercises/1"};

        for (String invalidPath : invalidPathArr) {
            IllegalHttpRequestPathException exception = assertThrows(IllegalHttpRequestPathException.class
                    , () -> HttpRequestValidator.checksPathValid(invalidPath));
        }
    }

    @Test
    @DisplayName("URI 유효성 테스트 - valid path")
    void checksPathValidTest() {
        String[] validPathArr = {"/tasks", "/tasks/1", "/tasks/31273612783", "/tasks/"};

        for (String validPath : validPathArr) {
            assertDoesNotThrow(() -> HttpRequestValidator.checksPathValid(validPath));
        }
    }
}