package com.codesoom.assignment.utils;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JsonParser {

    private ObjectMapper objectMapper = new ObjectMapper();

    public Task toTask(String content) {
        Task task = null;

        try {
            task = objectMapper.readValue(content, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return task;
    }

    public String toJSON(Object object)  {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            objectMapper.writeValue(outputStream, object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toString();
    }

}
