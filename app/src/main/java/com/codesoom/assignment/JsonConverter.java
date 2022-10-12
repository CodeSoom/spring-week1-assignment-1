package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

public final class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private static String tasksToJson(Collection<Task> taskCollection) throws JsonProcessingException {
        return objectMapper.writeValueAsString(taskCollection);
    }

    private static String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
