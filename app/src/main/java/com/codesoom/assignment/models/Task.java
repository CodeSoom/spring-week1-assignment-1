package com.codesoom.assignment.models;

public class Task {
    private Long id;
    private String title;

    public Task() {
        this.id = TaskIdGenerator.generateSequence();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
