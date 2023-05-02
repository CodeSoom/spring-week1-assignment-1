package com.codesoom.assignment.domain.task.model;

public class Task {

    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("id = ").append(id)
                .append('\'')
                .append("title = ").append(title)
                .toString();
    }

}
