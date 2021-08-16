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
            response = handleGetAll();
        }

        // GET Detail
        if ("GET".equals(method) && path.hasIdOf("tasks")) {
            response = handleGetOne(path);
        }

        // POST
        if ("POST".equals(method) && path.has("tasks")) {
            response = handlePost(body);
        }

        // PUT & PATCH
        if (("PUT".equals(method) || "PATCH".equals(method)) && path.hasIdOf("tasks")) {
            response = handlePut(path, body);
        }

        // DELETE
        if ("DELETE".equals(method) && path.hasIdOf("tasks")) {
            response = handleDelete(path);
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

    private Response handlePost(String body) {
        String content;
        int statusCode;

        try {
            content = taskService.create(body);
            statusCode = 201;
        } catch (IOException e) {
            content = e.toString();
            statusCode = 500;
        }

        return new Response(statusCode, content);
    }

    public Response handleGetAll() {
        String content;
        int statusCode;

        try {
            content = taskService.getAll();
            statusCode = 200;
        } catch (IOException e) {
            content = e.toString();
            statusCode = 500;
        }

        return new Response(statusCode, content);
    }

    public Response handleGetOne(Path path) {
        String content;
        int statusCode;

        try {
            content = taskService.getOne(path);
            statusCode = 200;
        } catch (NoSuchElementException | IOException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }

    public Response handleDelete(Path path) {
        String content;
        int statusCode;

        try {
            content = taskService.remove(path);
            statusCode = 204;
        } catch (NoSuchElementException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }

    public Response handlePut(Path path, String body) {
        String content;
        int statusCode;

        try {
            content = taskService.update(path, body)
            statusCode = 200;
        } catch(NoSuchElementException | IOException e) {
            content = e.toString();
            statusCode = 404;
        }

        return new Response(statusCode, content);
    }
}
