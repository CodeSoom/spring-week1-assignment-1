package com.codesoom.assignment.utils;

import java.util.Objects;

public class StringChecker {

    public static boolean isEqual(String reqPath, String mappedPath) {
        return Objects.equals(reqPath, mappedPath);
    }

    public static boolean isDigit(String value) {
        return value.chars().allMatch(Character::isDigit);
    }

}
