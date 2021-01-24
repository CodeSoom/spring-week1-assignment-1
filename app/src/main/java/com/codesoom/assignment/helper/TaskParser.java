package com.codesoom.assignment.helper;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * Parse related with task.
 */
public class TaskParser {
    ObjectMapper objectMapper = new ObjectMapper();

    public String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    public String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    public Task toTask(String content, Long id) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(id);
        return task;
    }

    public Task getTask(List<Task> tasks, Long taskId) {
        return tasks.stream()
                .filter(element -> Objects.equals(taskId, element.getId()))
                .findFirst()
                .orElse(new Task());
    }
}
