package com.codesoom.assignment.models;

public class Task {
    public static Long sequence = 0L;
    private Long id;
    private String title;

    public Task() {
        sequence++;
        this.id = sequence;
    }

    public Long getId() {
        return id;
    }

    public static Long getSequence() {
        return sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
