package com.codesoom.assignment.models;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", createdAt=" + createdAt +
            ", lastUpdatedAt=" + lastUpdatedAt +
            '}';
    }
}
