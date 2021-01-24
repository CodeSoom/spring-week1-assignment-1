package com.codesoom.assignment.web;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class PathPatternTest {
    String str1 = "/task";
    String str2 = "/task/";
    String str3 = "/task/1";
    String str4 = "/task/1/";

    @Test
    public void test() {
        String pattern = "^/(task)/([0-9]*)/?$";
        assertTrue(Pattern.matches(pattern, str3));
    }
}
