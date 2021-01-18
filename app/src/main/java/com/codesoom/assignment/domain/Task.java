package com.codesoom.assignment.domain;

public class Task {
    long id;
    String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Task updateTaskTitle(String newTitle) {
        return new Task(id, newTitle);
    }
}
