package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class JsonConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Task jsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String tasksToJSON(List<Map<String, Task>>  tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    public String oneTaskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }
}
