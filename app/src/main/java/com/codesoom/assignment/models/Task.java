package com.codesoom.assignment.models;


public class Task {

    private Long id;
    private String title;

    public Task() {
    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public Task updateTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
