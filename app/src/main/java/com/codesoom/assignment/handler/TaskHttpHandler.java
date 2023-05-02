package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.handler.TaskApiRoute.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.*;
import static com.codesoom.assignment.util.JsonUtil.*;

public class TaskHttpHandler implements HttpRequestHandler {

    private static final List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        TaskApiRoute endpoint = matchRoute(getRequestMethod(exchange), getRequestPath(exchange));
        ApiRouteHandler apiRouteHandler = endpoint.getHandlerMethod();
        apiRouteHandler.handle(exchange);
    }

    protected static void handleGetMethod(final HttpExchange exchange) throws IOException {
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(tasks));
    }

    protected static void handlePostMethod(final HttpExchange exchange) throws IOException {
        tasks.add(jsonToObject(extractRequestBody(exchange), Task.class));
        sendHttpResponse(exchange, CREATED.getCode(), objectToJsonString(tasks));
    }

    protected static void handlePutMethod(final HttpExchange exchange) throws IOException  {

    }

    protected static void handleDeleteMethod(final HttpExchange exchange) throws IOException {

    }

    protected static void handlePathNotFound(final HttpExchange exchange) throws IOException {
        String errorMessage = new StringBuilder()
                .append("404 Not Found: The requested path '")
                .append(getRequestPath(exchange))
                .append("' with method '")
                .append(getRequestMethod(exchange))
                .append("' does not exist.")
                .toString();
        sendHttpResponse(exchange, NOT_FOUND.getCode(), errorMessage);
    }

}
