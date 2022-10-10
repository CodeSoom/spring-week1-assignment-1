package com.codesoom.assignment.models;

public class Task {

    private static Long sequence = 1L;
    private Long id;
    private String title;

    public Task() {

    }

    public Long getId() {
        return id;
    }

    public void setId() {
        this.id = sequence++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
