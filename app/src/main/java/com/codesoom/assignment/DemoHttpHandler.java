package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    private static final int HTTP_OK_CODE = 200;
    private static final int HTTP_CREATE_CODE = 201;
    private static final int HTTP_NO_CONTENT_CODE = 204;
    private final List<Task> tasks;

    public DemoHttpHandler(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, IllegalArgumentException {
        String content = "basic content";

        HttpMethod method = getHttpMethod(exchange);
        String path = getHttpRequestPath(exchange);
        String body = getHttpRequestBody(exchange);

        if (method == GET && path.equals("/tasks")) {
            content = tasksToJson();
            exchange.sendResponseHeaders(HTTP_OK_CODE, content.getBytes().length);
        }

        if (method == POST && path.equals("/tasks")) {
            content = "content with POST " + body;
            exchange.sendResponseHeaders(HTTP_CREATE_CODE, content.getBytes().length);
        }

        if (method == PATCH && path.startsWith("/tasks")) {
            int taskId = extractTaskIdFromPath(path);
            content = "content with PATCH " + taskId;
            exchange.sendResponseHeaders(HTTP_OK_CODE, content.getBytes().length);
        }

        if (method == DELETE && path.startsWith("/tasks")) {
            int taskId = extractTaskIdFromPath(path);
            content = "content with DELETE " + taskId;
            exchange.sendResponseHeaders(HTTP_NO_CONTENT_CODE, content.getBytes().length);
        }

        sendResponse(exchange, content);
    }

    private String tasksToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private void sendResponse(HttpExchange exchange, String content) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private int extractTaskIdFromPath(String path) {
        String taskId = path.split("/")[2];
        return Integer.parseInt(taskId);
    }

    private String getHttpRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("/n"));
    }

    private String getHttpRequestPath(HttpExchange exchange) {
        // null 처리 분기 추가
        URI uri = exchange.getRequestURI();
        if (uri == null) {
            throw new IllegalArgumentException("failed to return URI");
        }

        String path = uri.getPath();
        if (path == null) {
            throw new IllegalArgumentException("failed to return request path");
        }

        return path;
    }

    private HttpMethod getHttpMethod(HttpExchange exchange) {
        String methodInString = exchange.getRequestMethod();
        return Arrays.stream(HttpMethod.values())
                .filter((method) ->
                        method.toString().equalsIgnoreCase(methodInString)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "failed to return HttpMethod enum value"
                        )
                );
    }
}
