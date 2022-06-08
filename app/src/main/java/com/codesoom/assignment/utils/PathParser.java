package com.codesoom.assignment.utils;

public class PathParser {
    public static Boolean isReqGettingAllTasks(String path) {
        return path.matches("/tasks/?");
    }

    public static Boolean isReqGettingOneTask(String path) {
        return path.matches("/tasks/\\d+/?");
    }

    public static Boolean isReqRegisterOneTask(String path) {
        return path.matches("/tasks/?");
    }

    public static Boolean isReqModifyOneTask(String path) {
        return path.matches("/tasks/\\d+?");
    }

    public static Boolean isReqDeleteOneTask(String path) {
        return path.matches("/tasks/\\d+?");
    }

    public static Long parseId(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

}
