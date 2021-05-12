package com.codesoom.assignment.enums;

public enum TodoURI {
    TASKS("/tasks$"),
    TASKS_ID("/tasks/\\d+$");

    private String uri;

    TodoURI(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }
}
