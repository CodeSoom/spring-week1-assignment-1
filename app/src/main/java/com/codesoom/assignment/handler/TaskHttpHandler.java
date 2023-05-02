package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Task;
import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.handler.TaskApiRoute.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.*;
import static com.codesoom.assignment.util.JsonUtil.*;

public class TaskHttpHandler implements HttpRequestHandler {

    private static final Tasks tasks = new Tasks();

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        ApiRouteHandler apiRouteHandler =
                matchRoute(getRequestMethod(exchange), getRequestPath(exchange)).getHandlerMethod();
        apiRouteHandler.handle(exchange);
    }

    protected static void handleGetOneMethod(final HttpExchange exchange) throws IOException {
        long id = parseIdFromPath(getRequestPath(exchange));
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(tasks.findById(id)));
    }

    protected static void handleGetAllMethod(final HttpExchange exchange) throws IOException {
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(tasks.getAll()));
    }

    protected static void handlePostMethod(final HttpExchange exchange) throws IOException {
        tasks.add(jsonToObject(extractRequestBody(exchange), Task.class));
        sendHttpResponse(exchange, CREATED.getCode(), objectToJsonString(tasks.getAll()));
    }

    protected static void handlePutMethod(final HttpExchange exchange) throws IOException  {
        Task task = tasks.update(
                parseIdFromPath(getRequestPath(exchange)),
                jsonToObject(extractRequestBody(exchange), Task.class).getTitle()
        );
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(task));
    }

    protected static void handleDeleteMethod(final HttpExchange exchange) throws IOException {
        tasks.delete(parseIdFromPath(getRequestPath(exchange)));
        sendHttpResponse(exchange, OK.getCode(), "");
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
