package com.codesoom.assignment.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringCheckerTest {

    @Test
    void isEqual_같을때() {
        boolean result = StringChecker.isEqual("/tasks/1", "/tasks/1");

        Assertions.assertTrue(result);
    }

    @Test
    void isEqual_같지_않을때() {
        boolean result = StringChecker.isEqual("/tasks", "/tasks/1");

        Assertions.assertFalse(result);
    }

}
