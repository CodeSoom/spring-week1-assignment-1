package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonConverter {
    public static Task toTask(String content) throws JsonProcessingException {
        return new ObjectMapper().readValue(content, Task.class);
    }

    public static String taskToJson(Task task) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    public static String tasksToJSON(Tasks tasks) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.getTasks());
        return outputStream.toString();
    }

    public static String extractValue(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Task task = objectMapper.readValue(content, Task.class);
        return task.getTitle();
    }
}
