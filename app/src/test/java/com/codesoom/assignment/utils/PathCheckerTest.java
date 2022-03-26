package com.codesoom.assignment.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathCheckerTest {

    @Test
    void isMatchedPath_일치할때() {
        boolean isMatched = PathChecker.isMatchedPath("/tasks/", "/tasks/");

        assertTrue(isMatched);
    }

    @Test
    void isMatchedPath_일치하지_않을때() {
        boolean isMatched = PathChecker.isMatchedPath("/tasks/1", "/tasks/");

        assertFalse(isMatched);
    }

    @Test
    void isMatchedPath_숫자_path() {
        boolean isMatched = PathChecker.isMatchedPath("/tasks/1", "/tasks/{id}");

        assertTrue(isMatched);
    }

}
