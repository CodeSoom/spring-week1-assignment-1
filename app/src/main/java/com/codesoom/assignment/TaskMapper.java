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

    public Task getTaskFromContent(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String tasksToJson() throws IOException {
        TaskManager taskManager = TaskManager.getInstance();
        List<Task> allTasks = taskManager.getAllTasks();

        return tasksToJson(allTasks);
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
