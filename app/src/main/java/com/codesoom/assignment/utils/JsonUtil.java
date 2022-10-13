package com.codesoom.assignment.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readValue(String json, Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(json, classType);
    }

    public static <T> String writeValue(T object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }
}
