package com.codesoom.assignment.models;

public class Task {
    private final long id;
    private final String title;

    public Task(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long id() {
        return this.id;
    }

    public String title() {
        return this.title;
    }
}
