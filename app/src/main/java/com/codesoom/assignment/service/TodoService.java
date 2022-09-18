package com.codesoom.assignment.service;

import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface TodoService {

    ResponseData processRequest(HttpExchange exchange, Task task, String pathVariable) throws IOException;

    default String convertToJSON(Object tasks) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
