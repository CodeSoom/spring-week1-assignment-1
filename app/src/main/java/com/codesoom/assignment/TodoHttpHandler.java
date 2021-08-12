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

        System.out.println(body);
        if (HttpMethod.GET.getHttpMethod().equals(method)) {
            todoHttpMethods.handleBasicGetMethod(path, exchange, taskMap);
            return;
        }
        if (HttpMethod.POST.getHttpMethod().equals(method)) {
            todoHttpMethods.handleGetMethodWithParameter(exchange, body, taskMap);
            return;
        }
        if (HttpMethod.PUT.getHttpMethod().equals(method)) {
            todoHttpMethods.handlePutMethod(path, exchange, body, taskMap);
            return;
        }
        if (HttpMethod.PATCH.getHttpMethod().equals(method)) {
            todoHttpMethods.handlePatchMethod(path, exchange, body, taskMap);
            return;
        }
        if (HttpMethod.DELETE.getHttpMethod().equals(method)) {
            todoHttpMethods.handleDeleteMethod(path, exchange, taskMap);
            return;
        }
    }
}
