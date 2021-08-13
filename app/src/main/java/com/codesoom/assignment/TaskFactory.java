package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskFactory {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Task toTask(String title) throws JsonProcessingException {
        return objectMapper.readValue(title, Task.class);
    }
}
