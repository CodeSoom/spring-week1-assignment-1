package com.codesoom.assignment.domain.task.model;

import com.codesoom.assignment.exception.InvalidValueException;

public class Task {

    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidValueException("Title cannot be null or empty");
        }
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("id = %s, title = %s", id, title);
    }

}
