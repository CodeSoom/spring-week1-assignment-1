package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;

public final class JsonConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonConverter() {
    }

    public static String jsonToTitle(String content) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(content);
        JsonNode valueNode = jsonNode.get("title");
        return valueNode.textValue();
    }

    public static String tasksToJson(Collection<Task> taskCollection) throws JsonProcessingException {
        return objectMapper.writeValueAsString(taskCollection);
    }

    public static String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
