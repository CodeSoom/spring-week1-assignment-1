package com.codesoom.assignment;

import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // get method, url, body
        String method = extractMethod(exchange);
        Path path = extractPath(exchange);
        String body = extractBody(exchange);

        Response response = new Response();
        // GET ALL
        if ("GET".equals(method) && path.has("tasks")) {
            response = taskService.getAll();
        }

        // GET Detail
        if ("GET".equals(method) && path.hasIdOf("tasks")) {
            response = taskService.getOne(path);
        }

        // POST
        if ("POST".equals(method) && path.has("tasks")) {
            response = taskService.create(body);
        }

        // PUT & PATCH
        if (("PUT".equals(method) || "PATCH".equals(method)) && path.hasIdOf("tasks")) {
            response = taskService.update(path, body);
        }

        // DELETE
        if ("DELETE".equals(method) && path.hasIdOf("tasks")) {
            response = taskService.remove(path);
        }

        exchange.sendResponseHeaders(response.getStatusCode(), response.getContent().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String extractMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    private Path extractPath(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        return new Path(uri);
    }

    private String extractBody(HttpExchange exchange) throws UnsupportedEncodingException {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
