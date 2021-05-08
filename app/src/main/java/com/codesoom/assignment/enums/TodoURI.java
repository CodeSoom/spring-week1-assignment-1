package com.codesoom.assignment.enums;

public enum TodoURI {
    TASKS("/tasks$"),
    PUT_TASKS("/tasks/\\d$");

    private String uri;

    TodoURI(String uri) {
        this.uri = uri;
    }

    public String uri() {
        return uri;
    }
}
