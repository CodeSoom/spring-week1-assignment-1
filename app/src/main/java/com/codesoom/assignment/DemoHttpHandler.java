package com.codesoom.assignment;

import com.codesoom.assignment.resources.ResourceFactory;
import com.codesoom.assignment.resources.TaskResource;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = handleRequest(method, path, body);

        if (content.equals(HttpStatusCode.BAD_REQUEST.getStatus())) {
            sendResponseHeaders(exchange, content, HttpStatusCode.NOT_FOUND);
        } else {
            sendResponseHeaders(exchange, content, HttpStatusCode.OK);
        }

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void sendResponseHeaders(HttpExchange exchange, String content, HttpStatusCode notFound) throws IOException {
        exchange.sendResponseHeaders(notFound.getCode(), content.getBytes().length);
    }

    private String handleRequest(String method, String path, String body)
            throws IOException {

        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if (HttpMethod.isProperMethod(httpMethod) && isProperPath(path)) {
            ResourceFactory factory = new ResourceFactory();
            TaskResource resource = factory.createResource(httpMethod);
            return resource.handleRequest(path, body);
        }

        return HttpStatusCode.BAD_REQUEST.getStatus();
    }

    public boolean isProperPath(String path) {
        return path.startsWith(URLs.TASKS);
    }
}
