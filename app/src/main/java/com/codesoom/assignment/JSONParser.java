package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class JSONParser {
    static String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(outputStream, task);
        return outputStream.toString();
    }

    static String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(outputStream, tasks);
        return  outputStream.toString();
    }

    static Task toTask(String content, Long id) throws JsonProcessingException {
        Task task = new ObjectMapper().readValue(content, Task.class);
        task.setId(id);
        return task;
    }
}
