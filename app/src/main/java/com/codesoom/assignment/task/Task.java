package com.codesoom.assignment.task;

public class Task {

    private Long id;
    private String title;

    public Task(){

    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }


}
