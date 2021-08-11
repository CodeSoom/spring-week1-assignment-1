package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public final class JsonConverter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Optional<Task> jsonToTask(final String content) {
        Task task = null;
        try {
            task = OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(task);
    }

    public static Optional<String> toJson(final Object object) {
        String jsonString = null;

        final OutputStream outputStream = new ByteArrayOutputStream();
        try {
            OBJECT_MAPPER.writeValue(outputStream, object);
            jsonString = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(jsonString);
    }

    public static Task jsonToTaskOrNull(final String content) {
        try {
            return OBJECT_MAPPER.readValue(content, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJsonOrNull(final Object object) {
        final OutputStream outputStream = new ByteArrayOutputStream();
        try {
            OBJECT_MAPPER.writeValue(outputStream, object);
            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
