package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class TodoHttpHandlerUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Title toTitle(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Title.class);
    }

    public static Long getId(String path) {
        String[] splits = path.split("/");
        return Long.parseLong(splits[splits.length - 1]);
    }

    public static String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    public static Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public static String tasksToJSON(Map<Long, Task> taskMap) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskMap.values());

        return outputStream.toString();
    }
}
