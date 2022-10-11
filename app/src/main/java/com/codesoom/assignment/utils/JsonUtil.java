package com.codesoom.assignment.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonUtil {
    static final private ObjectMapper objectMapper = new ObjectMapper();

    static public <T> T readValue(String json, Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(json, classType);
    }

    static public <T> String writeValue(T object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }
}
