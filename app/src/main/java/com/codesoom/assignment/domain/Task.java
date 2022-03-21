package com.codesoom.assignment.domain;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

public class Task {

    private String taskId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Task(TaskBuilder taskBuilder) {
        if (StringUtils.isNotEmpty(taskBuilder.getTaskId())) {
            modifyAction(taskBuilder);
        } else {
            createdAction(taskBuilder);
        }
    }

    private void modifyAction(TaskBuilder taskBuilder) {
        this.taskId = taskBuilder.getTaskId();
        this.title = taskBuilder.getTitle();
        this.updatedAt = LocalDateTime.now();
    }

    private void createdAction(TaskBuilder taskBuilder) {
        this.taskId = getTaskId();
        this.title = taskBuilder.getTitle();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    private String getTaskId() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public static class TaskBuilder {
        private final String title;
        private String taskId;
        private LocalDateTime updatedAt;

        public TaskBuilder(String title) {
            this.title = title;
        }

        private void updateTask(String taskId) {
            this.taskId = taskId;
            updatedAt = LocalDateTime.now();
        }

        public String getTaskId() {
            return taskId;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public String getTitle() {
            return title;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
