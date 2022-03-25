package com.codesoom.assignment.models;

public class Task {
    private final String delimiter = " ";

    private Long id;

    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return id + delimiter + title;
    }

    public String getDelimiter() {
        return delimiter;
    }

}
