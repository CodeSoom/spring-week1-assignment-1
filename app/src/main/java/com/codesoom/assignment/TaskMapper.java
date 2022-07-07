package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TaskMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String taskToString(Task task) throws IOException {
        return objectMapper.writeValueAsString(task);
    }

    public Task stringToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }
}
