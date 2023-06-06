package com.codesoom.assignment.task.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {}

    public static <T> T jsonToObject(final String content, final Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(content, classType);
    }

    public static <T> String objectToJsonString(final T object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

}
