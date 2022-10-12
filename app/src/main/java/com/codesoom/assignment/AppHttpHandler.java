package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
            return;
        }

        if (isRequiredPathVariable(method)) {
            if (isEmptyUserId(userId)) {
                ClientError.badRequest(exchange);
                return;
            }
        }

        if (isRequiredRequestBody(method)) {
            if (requestBody.isBlank()) {
                ClientError.badRequest(exchange);
                return;
            }
        }

        String data = "";
        switch (method) {
            case GET:
                if (isEmptyUserId(userId)) {
                    data = taskService.getTasks();
                } else {
                    data = taskService.getTaskByUserId(userId);
                }
                setResponse(exchange, HttpURLConnection.HTTP_OK, data);
                break;
            case POST:
                data = taskService.createTask(requestBody);
                setResponse(exchange, HttpURLConnection.HTTP_CREATED, data);
                break;
            case PUT:
            case PATCH:
                data = taskService.updateTask(userId, requestBody);
                setResponse(exchange, HttpURLConnection.HTTP_OK, data);
                break;
            case DELETE:
                taskService.deleteTask(userId);
                setResponse(exchange, HttpURLConnection.HTTP_OK, data);
                break;
            default:
                ClientError.notFound(exchange);
        }
    }

    private void setResponse(HttpExchange exchange, int responseCode, String data) throws IOException {
        exchange.sendResponseHeaders(responseCode, data.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(data.getBytes());
        responseBody.flush();
        responseBody.close();
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
        Long userId;
        if (pathArr.length > 2) {
            userId = Long.parseLong(pathArr[2]);
        } else {
            userId = 0L;
        }
        return userId;
    }

    private boolean isEmptyUserId(Long id) {
        return id.equals(0L);
    }
}
