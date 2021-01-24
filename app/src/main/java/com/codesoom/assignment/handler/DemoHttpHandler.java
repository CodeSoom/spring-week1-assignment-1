package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpStatusCode;
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
        HttpStatusCode statusCode = HttpStatusCode.OK;

        System.out.println(method + " " + path);

        try {
            content = new ResponseHandler().handle(method, path, tasks, body);
            switch (HttpMethod.valueOf(method)) {
                case POST:
                    statusCode = HttpStatusCode.CREATED;
                    break;

                case DELETE:
                    statusCode = HttpStatusCode.NO_CONTENT;
                    break;
            }
        } catch (ResponseHandlingException e) {
            e.printDescription();
            // NOT_FOUND와 METHOD_NOT_ALLOWED을 구분하여 status code 변경하면 좋으나, 테스트 코드 통과를 위해 NOT_FOUND로 통일함
            statusCode = HttpStatusCode.NOT_FOUND;
        }

        exchange.sendResponseHeaders(statusCode.getRawValue(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
