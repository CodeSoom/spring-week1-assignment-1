package com.codesoom.assignment.utils;

public final class HttpRequestValidator {

    public static boolean isPathValid(String path) {
        if (path == null || !path.contains("/")) {
            return false;
        }

        final String[] pathArr = path.split("/");
        if (!(pathArr.length == 2 || pathArr.length == 3)) {
            return false;
        }

        final String resourceName = pathArr[1];
        if (!resourceName.equals("tasks")) {
            return false;
        }

        if (pathArr.length == 2) {
            return true;
        }

        final String idPart = pathArr[2];
        return StringValidator.isNumber(idPart);
    }

    public static boolean isMethodNameValid(String methodName) {
        switch (methodName) {
            case "GET":
            case "POST":
            case "PUT":
            case "PATCH":
            case "DELETE":
                return true;
            default:
                return false;
        }
    }

}
