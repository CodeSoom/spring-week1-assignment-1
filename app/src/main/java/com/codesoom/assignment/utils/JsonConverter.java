package com.codesoom.assignment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public final class JsonConverter {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
