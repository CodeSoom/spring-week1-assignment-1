package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskMapper {
    public static Task toTask(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, Task.class);
    }

    public static String toString(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    public static String toString(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
