package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskJsonTransfer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Task jsonStringToTask(String jsonString) throws JsonProcessingException {
        JsonTask task = objectMapper.readValue(jsonString, JsonTask.class);
        return new Task(-1L, task.title);
    }

    public String taskToJson(Task task) throws JsonProcessingException {
        JsonTask jsonTask = new JsonTask(task.getId(), task.getTitle());
        return objectMapper.writeValueAsString(jsonTask);
    }
}
