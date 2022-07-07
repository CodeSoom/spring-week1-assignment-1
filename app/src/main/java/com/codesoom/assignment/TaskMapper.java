package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class TaskMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Task stringToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String taskToString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

    public String tasksToString(List<Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }
}
