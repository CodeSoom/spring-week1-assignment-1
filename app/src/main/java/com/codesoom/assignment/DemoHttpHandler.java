package com.codesoom.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DemoHttpHandler implements HttpHandler {
    private static final int OK = 200;
    private static final String GET_METHOD = "GET";
    private static final String TASKS_PATH = "/tasks";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (requestMethod.equals(GET_METHOD) && path.equals(TASKS_PATH)) {
            sendResponseBody(exchange, new ResponseData(OK, tasksToJson()));
            return;
        }
    }

    private void sendResponseBody(HttpExchange exchange, ResponseData responseData) throws IOException {
        String contents = responseData.contents();
        exchange.sendResponseHeaders(responseData.statusCode(), contents.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(contents.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
