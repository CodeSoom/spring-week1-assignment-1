package com.codesoom.assignment.task;

public class Task {

    private long id;
    private String title;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title=\"" + title + '\"' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
