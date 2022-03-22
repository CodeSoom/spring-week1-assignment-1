package com.codesoom.assignment.domain;


import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;

public class Task {

    private String taskId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Task(String taskId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Task registerTask(String title) {
        var taskId = RandomStringUtils.randomAlphabetic(8);
        var task = new Task(taskId, title, LocalDateTime.now(), LocalDateTime.now());

        return task;
    }

    public Task updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
        return this;
    }
}
