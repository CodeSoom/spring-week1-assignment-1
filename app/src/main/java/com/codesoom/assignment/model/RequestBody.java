package com.codesoom.assignment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RequestBody {
    private HttpExchange exchange;
    private ObjectMapper objectMapper = new ObjectMapper();

    private RequestBody() {
    }

    public RequestBody(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public <T> T read(Class<T> type) throws JsonProcessingException {
        String body = parseBody();
        return objectMapper.readValue(body, type);
    }

    private String parseBody() {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
        return body;
    }

}
