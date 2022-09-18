package com.codesoom.assignment.model;

import java.util.Arrays;
import java.util.stream.Stream;

public class Path {

    public static final String REQUEST_MAPPING_URL = "/tasks";

    public static String getPathVariable(String pathVariable) {
        return extractPathVariable(pathVariable);
    }

    private static String extractPathVariable(String path) {
        if (path == null) {
            path = REQUEST_MAPPING_URL;
        }

        String[] splitPaths = path.replace(REQUEST_MAPPING_URL, "").split("/");

        if (splitPaths.length == 1 && splitPaths[0].isBlank()) {
            return null;
        }
        if (splitPaths.length != 2) {
            return "";
        }
        if (!isNumeric(splitPaths[1])) {
            return "";
        }

        return splitPaths[1];
    }

    private static boolean isNumeric(String value) {
        long count = value.chars()
                .filter(asc -> !(asc >= 48 && asc <= 57))
                .count();

        return count == 0;
    }
}
