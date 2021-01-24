package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String toJSON(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

}
