package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.ServerConfiguration;
import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.ResponseHandlingException;
import com.codesoom.assignment.model.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle related with Task object.
 */
public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String content = "";
        int statusCode = HttpStatusCode.OK;

        System.out.println(method + " " + path);

        try {
            content = new ResponseHandler().handle(method, path, tasks, body);

            switch (method) {
                case HttpMethod.POST:
                    statusCode = HttpStatusCode.CREATED;
                    break;

                case HttpMethod.DELETE:
                    statusCode = HttpStatusCode.NO_CONTENT;
                    break;
            }
        } catch (ResponseHandlingException e) {
            e.printDescription();
            statusCode = HttpStatusCode.NOT_FOUND;
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
