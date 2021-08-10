package com.codesoom.assignment.models;

public class Task {

    private Long id;
    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Task() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Task {id=%s, title=%s} ", id, title);
    }

    public boolean isMatchId(long taskId) {
        return this.id.equals(taskId);
    }
}
