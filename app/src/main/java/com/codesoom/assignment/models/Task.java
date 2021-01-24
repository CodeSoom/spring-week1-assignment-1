package com.codesoom.assignment.models;

public class Task {

    private long Id = 1;
    private String Title;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) { Title = title; }

}
