package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskByIdHandler extends TaskHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println(method);
        if(Objects.equals(method, "GET")){
            handleGetMethod(exchange);
        }else if(Objects.equals(method, "PUT") || Objects.equals(method, "PATCH")){
            handlePutOrPatchMethod(exchange);
        }else if(Objects.equals(method, "DELETE")){
            handleDeleteMethod(exchange);
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        String content = "Task: GET";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePutOrPatchMethod(HttpExchange exchange) throws IOException {
        String content = "Task: PUT or PATCH";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handleDeleteMethod(HttpExchange exchange) throws IOException {
        String content = "Task: DELETE";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }
}
