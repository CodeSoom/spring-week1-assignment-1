package com.codesoom.assignment.utils;

public class StringUtil {

    public static boolean isNumeric(String str) {
        return str.chars()
                .allMatch(Character::isDigit);
    }
}
