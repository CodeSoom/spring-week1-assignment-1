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

    public boolean isTaskId(int id) {
        return this.id == id;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
