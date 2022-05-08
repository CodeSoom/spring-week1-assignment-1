package com.codesoom.assignment;

import com.codesoom.assignment.method.CreateTask;
import com.codesoom.assignment.method.DeleteTask;
import com.codesoom.assignment.method.GetAllTask;
import com.codesoom.assignment.method.GetTask;
import com.codesoom.assignment.method.PutTask;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseCreated;
import com.codesoom.assignment.response.ResponseNoContent;
import com.codesoom.assignment.response.ResponseOK;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private int id = 1;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            handleCollection(exchange);
        } else if (path.startsWith("/tasks/")) {
            handleItem(exchange);
        }
    }

    private void handleCollection(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            new GetAllTask(exchange).handleItem();
        } else if (method.equals("POST")) {
            new CreateTask(exchange).handleItem();
        }
    }

    private void handleItem(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            new GetTask(exchange).handleItem();
        } else if (method.equals("PUT")) {
            new PutTask(exchange).handleItem();
        } else if (method.equals("DELETE")) {
            new DeleteTask(exchange).handleItem();
        }
    }

    private void canConvertId(String idString) {
        try {
            Long.valueOf(idString);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

}
