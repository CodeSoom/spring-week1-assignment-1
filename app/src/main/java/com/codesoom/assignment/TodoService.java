package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TodoService {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long sequence = 0L;


    public String getTasks() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    public String postTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content,Task.class);
        tasks.add(task);
        return "success";

    }

    public String putTasks(Long taskId, String body) {
    }

    public String getTask(Long taskId) {
    }

    public String deleteTask(Long taskId) {
    }
}
