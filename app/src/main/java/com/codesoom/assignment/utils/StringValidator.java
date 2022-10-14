package com.codesoom.assignment.utils;

public final class StringValidator {

    public static boolean isNumberFormat(String s) {
        for (char c : s.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }
}
