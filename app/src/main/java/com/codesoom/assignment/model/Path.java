package com.codesoom.assignment.model;

public class Path {

    public static final String REQUEST_MAPPING_URL = "/tasks";

    public static String getPathVariable(String pathVariable) {
        return extractPathVariable(pathVariable);
    }

    private static String extractPathVariable(String path) {
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
        try {
            Long tempId = Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
