package com.codesoom.assignment.utils;

public class StringUtil {

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        if (!str.equals("0") && str.startsWith("0")) {
            return false;
        }

        return str.chars()
                .allMatch(Character::isDigit);
    }

    public static boolean isOverThanZero(String str) {
        if (isNull(str)) {
            return false;
        }

        if (!isNumeric(str)) {
            return false;
        }

        return Integer.parseInt(str) > 0;
    }

    public static boolean startWithZero(String str) {
        if (isNull(str)) {
            return false;
        }

        return str.startsWith("0");
    }

    public static boolean isNull(String str) {
        return str == null;
    }
}
