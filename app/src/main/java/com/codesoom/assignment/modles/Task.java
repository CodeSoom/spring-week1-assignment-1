package com.codesoom.assignment.modles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public final class Task {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Long CURRENT_ID = 1L;

    private final Long id;
    private String title;

    public Task() {
        this.id = CURRENT_ID;
        ++CURRENT_ID;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Optional<Task> jsonToTask(final String content) {
        Task task = null;
        try {
            task = OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
            --CURRENT_ID;
        }
        return Optional.ofNullable(task);
    }

    public static Task jsonToTaskOrNull(final String content) {
        try {
            return OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            --CURRENT_ID;
            return null;
        }
    }
}
