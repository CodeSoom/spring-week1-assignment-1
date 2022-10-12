package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.utils.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppHttpHandler implements HttpHandler {
    private final TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethodType method = HttpMethodType.valueOf(exchange.getRequestMethod());
        String[] pathArr = exchange.getRequestURI().getPath().split("/");
        String path = pathArr[1];
        Long userId = getUserId(pathArr);
        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));

        if (!path.equals("tasks")) {
            ClientError.notFound(exchange);
        }

        if (isRequiredPathVariable(method) && isEmptyUserId(userId)) {
            ClientError.badRequest(exchange);
        }

        if (isRequiredRequestBody(method) && requestBody.isBlank()) {
            ClientError.badRequest(exchange);
        }

        if (method == HttpMethodType.GET) {
            if (isEmptyUserId(userId)) {
                List<Task> tasks = taskService.getTasks();
                setResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(tasks));
            } else {
                Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

                if (taskByUserId.isEmpty()) {
                    ClientError.notFound(exchange);
                }

                setResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(taskByUserId.get()));
            }
            return;
        }

        if (method == HttpMethodType.POST) {
            Task task = taskService.createTask(requestBody);
            setResponse(exchange, HttpURLConnection.HTTP_CREATED, JsonUtil.writeValue(task));
            return;
        }

        if (method == HttpMethodType.PUT || method == HttpMethodType.PATCH) {
            Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

            if (taskByUserId.isEmpty()) {
                ClientError.notFound(exchange);
            }

            Task task = taskService.updateTask(taskByUserId.get(), requestBody);
            setResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(task));
            return;
        }

        if (method == HttpMethodType.DELETE) {
            Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

            if (taskByUserId.isEmpty()) {
                ClientError.notFound(exchange);
            }

            taskService.deleteTask(taskByUserId.get());
            setResponse(exchange, HttpURLConnection.HTTP_OK);
            return;
        }
    }

    private void setResponse(HttpExchange exchange, int responseCode, String data) throws IOException {
        exchange.sendResponseHeaders(responseCode, data.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(data.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private void setResponse(HttpExchange exchange, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        exchange.getResponseBody().close();
    }

    private boolean isRequiredRequestBody(HttpMethodType method) {
        return method == HttpMethodType.POST
                || method == HttpMethodType.PUT
                || method == HttpMethodType.PATCH;
    }

    private boolean isRequiredPathVariable(HttpMethodType method) {
        return method == HttpMethodType.PUT
                || method == HttpMethodType.PATCH
                || method == HttpMethodType.DELETE;
    }

    private Long getUserId(String[] pathArr) {
        if (isEmptyPathVariable(pathArr)) {
            return 0L;
        }

        return Long.parseLong(pathArr[2]);
    }

    private boolean isEmptyPathVariable(String[] pathArr) {
        return pathArr.length <= 2;
    }

    private boolean isEmptyUserId(Long id) {
        return id.equals(0L);
    }
}
