package com.codesoom.assignment;

import com.codesoom.assignment.models.Tasks;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private Tasks tasks = new Tasks();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        HttpMethod method = HttpMethod.findByHttpMethod(httpExchange.getRequestMethod());
        URI uri = httpExchange.getRequestURI();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Resource resource = new Resource(uri.getPath(), Resource.makeBodyContent(body));

        System.out.println(method + " " + resource.getPath());

        String content = method.operate(resource, tasks);

        StatusCode statusCode = StatusCode.OK;
        if(method.getMethod().equals("POST")) {
            statusCode = StatusCode.Created;
        }

        httpExchange.sendResponseHeaders(statusCode.getStatusNumber(), content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}