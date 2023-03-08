package com.codesoom.assignment.models;

public class Task {
    private int id;
    private String title;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isTaskId(int requestTaskId) {
        return this.id == requestTaskId;
    }

    public void updateTitle(String requestTitle) {
        this.title = requestTitle;
    }
}
