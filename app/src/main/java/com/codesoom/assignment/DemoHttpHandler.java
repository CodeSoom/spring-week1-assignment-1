package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static com.codesoom.assignment.HttpMethod.GET;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethod method = getHttpMethod(exchange);
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String content = "basic content";

        if (method == GET && path.equals("/tasks")) {
            content = "content with GET";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getHttpRequestPath(HttpExchange exchange) {
        // null 처리 분기 추가
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

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