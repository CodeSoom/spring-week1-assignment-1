package com.codesoom.assignment.todolist.domain;

public class Task {
    private Long id;
    private String title;

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String updated) {
        if (!title.equals(updated)) {
            title = updated;
        }
    }
}
