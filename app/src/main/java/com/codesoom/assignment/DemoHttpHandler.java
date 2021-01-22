package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle related with Task object
 */
public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

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

        String content = "";
        int statusCode = Constant.HttpStatusCode.OK;

        try {
            content = new ResponseHandler().handle(method, path, tasks, body);

            switch (method) {
                case "POST":
                    statusCode = Constant.HttpStatusCode.CREATED;
                    break;

                case "DELETE":
                    statusCode = Constant.HttpStatusCode.NO_CONTENT;
                    break;
            }

        } catch (ResponseHandlingException e) {
            e.printDescription();
            content = "";
            statusCode = Constant.HttpStatusCode.NOT_FOUND;
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
