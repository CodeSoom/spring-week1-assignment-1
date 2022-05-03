package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static com.codesoom.assignment.HttpMethod.GET;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException, IllegalArgumentException {
        String content = "basic content";

        HttpMethod method = getHttpMethod(exchange);
        String path = getHttpRequestPath(exchange);

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
