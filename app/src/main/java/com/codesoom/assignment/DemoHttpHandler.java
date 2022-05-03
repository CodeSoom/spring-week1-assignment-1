package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    private static final int HTTP_OK_CODE = 200;
    private static final int HTTP_CREATE_CODE = 201;
    private static final int HTTP_NO_CONTENT_CODE = 204;

    @Override
    public void handle(HttpExchange exchange) throws IOException, IllegalArgumentException {
        String content = "basic content";

        HttpMethod method = getHttpMethod(exchange);
        String path = getHttpRequestPath(exchange);
        String body = getHttpRequestBody(exchange);

        if (method == GET && path.equals("/tasks")) {
            content = "content with GET";
        }

        if (method == POST && path.equals("/tasks")) {
            content = "content with POST " + body;
        }

        if (method == PATCH && path.startsWith("/tasks")) {
            int taskId = extractTaskIdFromPath(path);
            content = "content with PATCH " + taskId;
        }

        if (method == DELETE && path.startsWith("/tasks")) {
            int taskId = extractTaskIdFromPath(path);
            content = "content with DELETE " + taskId;
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

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
        if (uri == null) throw new IllegalArgumentException("failed to return URI");

        String path = uri.getPath();
        if (path == null) throw new IllegalArgumentException("failed to return request path");

        return path;
    }

    private HttpMethod getHttpMethod(HttpExchange exchange) {
        HttpMethod result = null;
        String methodInString = exchange.getRequestMethod();

        for (HttpMethod method : HttpMethod.values()) {
            if (method.toString().equalsIgnoreCase(methodInString)) {
                result = method;
                break;
            }
        }

        if (result == null) throw new IllegalArgumentException("failed to return HttpMethod enum value");

        return result;
    }
}
