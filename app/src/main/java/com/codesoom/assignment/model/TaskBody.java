package com.codesoom.assignment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TaskBody {
    private HttpExchange exchange;
    private ObjectMapper objectMapper = new ObjectMapper();

    private TaskBody() {
    }

    public TaskBody(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public Task read() throws JsonProcessingException {
        String body = parseBody();
        return objectMapper.readValue(body, Task.class);
    }

    private String parseBody() {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
        return body;
    }

}
