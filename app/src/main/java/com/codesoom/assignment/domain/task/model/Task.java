package com.codesoom.assignment.domain.task.model;

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
        this.title = title;
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
