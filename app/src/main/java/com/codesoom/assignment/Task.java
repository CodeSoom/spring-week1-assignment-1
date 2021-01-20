package com.codesoom.assignment;

public class Task {

    private static Long id = -1L;
    private String title;

    public Task(){

    }

    public Task(String title) {
        id++;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

}
