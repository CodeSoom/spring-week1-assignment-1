package com.codesoom.assignment;

import java.util.Objects;

public class Task {
    private Long id;
    private String title;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
