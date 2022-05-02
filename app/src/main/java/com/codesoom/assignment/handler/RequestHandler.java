package com.codesoom.assignment.handler;

import com.codesoom.assignment.models.MethodType;
import com.codesoom.assignment.models.StatusCode;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler implements HttpHandler {
    private final List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MethodType methodType = MethodType.valueOf(exchange.getRequestMethod());

        switch (methodType) {
            case GET:
                getTasksProcessor(exchange);
                break;
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 METHOD 타입입니다.");
        }

    }

    private void getTasksProcessor(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        if (uri.getPath().equals("/tasks")) {
            String content = objectMapper.writeValueAsString(tasks);
            printOutputStream(exchange, content, StatusCode.OK);
        }
    }

    private void printOutputStream(HttpExchange exchange, String content, StatusCode statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode.getStatusCode(), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
