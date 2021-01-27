package com.codesoom.assignment.models;

import java.util.OptionalLong;

public class TasksPath {
    private final String path;
    private static final String TASKS = "/tasks/";
    public TasksPath(String path) {
        this.path = path;
    }

    public Long extractNumber() {
        String replace = path.replace(TASKS, "");
        return OptionalLong.of(Long.parseLong(replace)).getAsLong();
    }
    public boolean isValidUrl(){
        return path.equals("/") || path.startsWith("/tasks");
    }

    public boolean hasNumberParameter() {
        String replace = path.replace(TASKS, "");
        return replace.matches("^[0-9]+$");
    }
}
