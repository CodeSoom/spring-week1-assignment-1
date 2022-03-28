package com.codesoom.assignment.task.dto;

import com.codesoom.assignment.task.Task;

public class TaskDto {

    private String title;

    public TaskDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task toTask() {
        return new Task(title);
    }
}
