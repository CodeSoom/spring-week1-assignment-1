package com.codesoom.assignment.models;

import java.util.Objects;

public class Task {

    private Long id;

    private String title;

    public Task() {}

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean equalTaskId(Long taskId) {
        return Objects.equals(id, taskId);
    }

    public boolean hasValidContent() {
        return title != null && !title.isEmpty();
    }
}
