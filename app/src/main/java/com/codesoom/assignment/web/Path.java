package com.codesoom.assignment.web;

import java.util.regex.Pattern;

public class Path {

    private static final String TASKS = "/tasks";

    private final String path;

    public Path(String path) {
        this.path = path;
    }

    public String getTaskId() {
        return path.replace(TASKS, "")
            .replace("/", "");
    }

    public boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    public boolean isTasks() {
        return TASKS.equals(path);
    }
}
