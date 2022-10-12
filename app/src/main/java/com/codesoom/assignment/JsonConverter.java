package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

public final class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson(Collection<Task> taskCollection) throws JsonProcessingException {
        return objectMapper.writeValueAsString(taskCollection);
    }

    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
