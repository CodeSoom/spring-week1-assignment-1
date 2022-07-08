package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TaskMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Task readTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String writeTaskAsString(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

    public String writeTasksAsString(List<Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }
}
