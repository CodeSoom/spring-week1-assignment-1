package com.codesoom.assignment.domain;

public class Task {
    private long id;
    private String title;

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

    @Override
    public String toString() {
        return "Task:"+ id;
    }
}
