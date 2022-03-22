package com.codesoom.assignment.domain.task;

import java.time.LocalDateTime;

// 응답시 task 객체
public class TaskInfo {
    private final String taskId;
    private final String title;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private TaskInfo(TaskInfoBuilder taskInfoBuilder) {
        this.taskId = taskInfoBuilder.getTaskId();
        this.title = taskInfoBuilder.getTitle();
        this.createdAt = taskInfoBuilder.getCreatedAt();
        this.updatedAt = taskInfoBuilder.getUpdatedAt();
    }

    public static class TaskInfoBuilder {
        private final String taskId;
        private final String title;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public TaskInfoBuilder(String taskId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.taskId = taskId;
            this.title = title;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public String getTitle() {
            return title;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        public String getTaskId() {
            return taskId;
        }

        public TaskInfo build() {
            return new TaskInfo(this);
        }
    }

}
