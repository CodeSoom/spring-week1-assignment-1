package com.codesoom.assignment;

public class Task {
    private Long id;
    private String title;

    Task() {
    }

    Task(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("{ id = %s, title = %s }", id, title);
    }
}
