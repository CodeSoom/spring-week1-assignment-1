package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    public Task getTaskFromContent(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String tasksToJson() throws IOException {
        List<Task> allTasks = new TaskManager().getAllTasks();
        return tasksToJson(allTasks);
    }
}
