package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TaskMapper {
    private ObjectMapper objectMapper = new ObjectMapper();

    public String taskToString(Task task) throws IOException {
        return objectMapper.writeValueAsString(task);
    }
}
