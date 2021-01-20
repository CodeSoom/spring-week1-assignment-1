package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpRequestMethod;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class DemoHttpHandler implements HttpHandler {

    private TaskService taskService = new TaskService();
    public static final String TASKS = "/tasks";
    public static final String TASKS_PATTERN = TASKS + "/*[0-9]*";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());

        if (!isValidRequest(httpRequest)) {
            sendResponse(exchange, createWrongMethodResponse());
            return;
        }

        HttpRequestMethod httpRequestMethod = httpRequest.getMethod();
        HttpResponse response = httpRequestMethod.createResponse(httpRequest, taskService);
        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private boolean isValidRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        HttpRequestMethod method = httpRequest.getMethod();

        return switch (method) {
            case GET -> path.equals(TASKS) || path.matches(TASKS_PATTERN);
            case POST -> path.equals(TASKS);
            case PATCH, PUT, DELETE -> path.matches(TASKS_PATTERN);
            default -> false;
        };
    }

    private HttpResponse createWrongMethodResponse() {
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_METHOD_NOT_ALLOWED);
    }

    private HttpResponse createWrongRequestResponse() {
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NOT_FOUND);
    }

}
