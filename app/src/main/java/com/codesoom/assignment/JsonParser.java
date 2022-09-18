package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonParser {
    private static ObjectMapper objectMapper = new ObjectMapper();


    public static <T> T requestBodyToObject(String body, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(body, type);
    }

    public static <T> String objectToJson(T object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }
}
