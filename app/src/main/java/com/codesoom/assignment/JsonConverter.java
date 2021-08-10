package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class JsonConverter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
