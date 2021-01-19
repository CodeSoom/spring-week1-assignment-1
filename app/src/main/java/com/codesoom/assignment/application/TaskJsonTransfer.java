package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

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

    public String taskListToJson(List<Task> tasks) throws JsonProcessingException {
        List<JsonTask> jsonTaskList = tasks
            .stream()
            .map(t -> new JsonTask(t.getId(), t.getTitle()))
            .collect(Collectors.toList());
        return objectMapper.writeValueAsString(jsonTaskList);
    }
}
