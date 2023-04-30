package com.codesoom.assignment.config;

import java.util.regex.Pattern;

public class Regex {
    private static final Pattern IS_ONLY_NUMBER = Pattern.compile("^[0-9]*?");

    public static boolean isOnlyNumber(final String str) {
        return IS_ONLY_NUMBER.matcher(str).matches();
    }
}
