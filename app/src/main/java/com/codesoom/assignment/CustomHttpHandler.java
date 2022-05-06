package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.codesoom.assignment.models.TaskResult;
import com.codesoom.assignment.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

public class CustomHttpHandler implements HttpHandler {

    private TaskManager taskManager = new TaskManager();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = setTask(path, requestBody);

        TaskResult result = null;
        if (isTask(path)) {
            result = (HttpMethodProcessor.valueOf(method)).process(task);
        }

        result.configureResponseHeaders(exchange);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(result.convertTaskToJson().getBytes());
        responseBody.flush();
    }

    private Task setTask(String path, String requestBody) throws JsonProcessingException {
        Task task = new Task();
        if (!requestBody.isBlank()) {
            task = JsonUtils.jsonToTask(requestBody);
        }

        if (hasTaskId(path)) {
            task.setId(Long.valueOf(path.split("/")[2]));
        }

        return task;
    }

    private boolean isTask(String path) {
        return path.startsWith("/tasks");
    }

    private boolean hasTaskId(String path) {
        return path.split("/").length > 2;
    }
}
