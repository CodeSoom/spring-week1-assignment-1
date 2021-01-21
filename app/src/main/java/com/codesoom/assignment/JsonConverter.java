package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonConverter {
    private ObjectMapper objectMapper;

    public JsonConverter() {
        this.objectMapper = new ObjectMapper();
    }

    public Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    public String tasksToJSON(Tasks tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.getTasks());
        return outputStream.toString();
    }

    public String extractValue(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        return task.getTitle();
    }
}
