package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskJsonTransfer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<Task> jsonStringToTask(String jsonString) {
        try {
            JsonTask task = objectMapper.readValue(jsonString, JsonTask.class);
            return Optional.of(new Task(-1L, task.title));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public Optional<String> taskToJson(Task task) {
        try {
            JsonTask jsonTask = new JsonTask(task.getId(), task.getTitle());
            return Optional.ofNullable(objectMapper.writeValueAsString(jsonTask));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public Optional<String> taskListToJson(List<Task> tasks) {
        try {
            List<JsonTask> jsonTaskList = tasks
                .stream()
                .map(t -> new JsonTask(t.getId(), t.getTitle()))
                .collect(Collectors.toList());
            return Optional.ofNullable(objectMapper.writeValueAsString(jsonTaskList));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
