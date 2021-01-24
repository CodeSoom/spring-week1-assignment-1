package com.codesoom.assignment;

import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.resources.ResourceFactory;
import com.codesoom.assignment.resources.TaskResource;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private final List<Task> tasks;

    public DemoHttpHandler(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Response response = handleRequest(method, path, body);

        exchange.sendResponseHeaders(response.getHttpStatusCode().getCode(), response.getContent().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(response.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Response handleRequest(String method, String path, String body)
            throws IOException {

        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if (HttpMethod.isProperMethod(httpMethod)) {
            ResourceFactory factory = new ResourceFactory();
            TaskResource resource = factory.createResource(httpMethod);
            return resource.handleRequest(tasks, path, body);
        }

        return new Response(HttpStatusCode.BAD_REQUEST.getStatus(), HttpStatusCode.BAD_REQUEST);
    }
}
