package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Task;

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
