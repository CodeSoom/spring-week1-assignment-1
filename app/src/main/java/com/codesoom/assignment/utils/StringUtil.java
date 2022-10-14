package com.codesoom.assignment.utils;

public class StringUtil {

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
