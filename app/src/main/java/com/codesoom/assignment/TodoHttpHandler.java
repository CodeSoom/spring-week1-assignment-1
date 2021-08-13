package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpMethod;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.TodoHttpMethods;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private final TodoHttpMethods todoHttpMethods = new TodoHttpMethods();
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        switch (HttpMethod.compare(method)) {
            case GET:
                todoHttpMethods.handleBasicGetMethod(path, exchange, taskMap);
                break;
            case POST:
                todoHttpMethods.handlePostMethodWithParameter(exchange, body, taskMap);
                break;
            case PUT:
                todoHttpMethods.handlePutMethod(path, exchange, body, taskMap);
                break;
            case PATCH:
                todoHttpMethods.handlePatchMethod(path, exchange, body, taskMap);
                break;
            case DELETE:
                todoHttpMethods.handleDeleteMethod(path, exchange, taskMap);
                break;
        }
    }
}
