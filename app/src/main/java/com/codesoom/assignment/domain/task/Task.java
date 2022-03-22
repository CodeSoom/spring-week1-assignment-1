package com.codesoom.assignment.domain.task;


import com.codesoom.assignment.domain.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {

    private String taskId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Task(String title) {
        this.taskId = generateTaskId();
        this.title = title;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private String generateTaskId() {
        return UUID.randomUUID()
                    .toString()
                    .replaceAll("-", "")
                    .substring(0, 8);
    }

    public Task updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getTaskId() {
        return taskId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public static class TaskBuilder implements Builder<Task> {
        private String title;

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public Task builder() {
            return new Task(title);
        }
    }
}
