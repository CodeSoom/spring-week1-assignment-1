package com.codesoom.assignment.utils;

public final class StringValidator {

    private StringValidator() {
    }

    /**
     * This method validates whether the given argument is suitable uri resource id.
     * Suitable uri resource id means not including any other characters than digit characters ('0' ~ '9')
     * and not starting with '0' except for "0" by itself.
     *
     * @param s a string to be validated
     * @return true if given argument is suitable, false if not suitable
     */
    public static boolean isNumberFormatValid(String s) {
        if (s.equals("0")) {
            return true;
        }

        if (s.startsWith("0")) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }
}
