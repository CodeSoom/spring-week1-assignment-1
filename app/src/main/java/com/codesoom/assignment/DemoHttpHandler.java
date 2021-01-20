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

        System.out.println(method + " " + path);
        String content = handleRequest(method, path, body);

        if (content.equals(HttpStatusCode.BAD_REQUEST.getStatus())) {
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), content.getBytes().length);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.OK.getCode(), content.getBytes().length);
        }

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handleRequest(String method, String path, String body)
            throws IOException {

        if (isProperMethod(method) && isProperPath(path)) {
            ResourceFactory factory = new ResourceFactory();
            TaskResource resource = factory.createResource(method);
            return resource.handleRequest(path, body);
        }

        return HttpStatusCode.BAD_REQUEST.getStatus();
    }

    private boolean isProperMethod(String method) {
        return method.equals(HttpMethod.GET.name()) ||
               method.equals(HttpMethod.POST.name()) ||
               method.equals(HttpMethod.PUT.name()) ||
               method.equals(HttpMethod.DELETE.name());
    }

    private boolean isProperPath(String path) {
        return path.startsWith(Constants.TASKS);
    }
}
