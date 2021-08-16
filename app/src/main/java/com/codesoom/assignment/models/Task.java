package com.codesoom.assignment.models;

import java.util.Optional;

public class Task {
    private long id;
    private String title;

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

    public String toString() {
        return "Task - title : " + title;
    }
}
