package com.codesoom.assignment.utils;

public class PathChecker {

    private static final String PATH_REGEX = "/";

    public static boolean isMatchedPath(String reqPath, String mappedPath) {
        String[] pathElements = reqPath.split(PATH_REGEX);
        String[] mappedPathElements = mappedPath.split(PATH_REGEX);

        if (pathElements.length != mappedPathElements.length) {
            return false;
        }

        if (pathElements.length == 2) {
            return StringChecker.isEqual(pathElements[1], mappedPathElements[1]);
        }

        if (pathElements.length == 3) {
            return StringChecker.isEqual(pathElements[1], mappedPathElements[1])
                    && StringChecker.isDigit(pathElements[2]);
        }

        return true;
    }


    public static Long getPathVariable(String reqPath, int index) {
        String[] pathElements = reqPath.split(PATH_REGEX);
        return Long.valueOf(pathElements[index]);
    }

}
