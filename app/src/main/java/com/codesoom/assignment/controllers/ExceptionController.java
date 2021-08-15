package com.codesoom.assignment.controllers;

import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.function.Consumer;

public class ExceptionController extends Controller {
    public void handleInvalidMethod(
            final HttpExchange exchange, final String path, final String[] allowedMethods
    ) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        final Consumer<String> methodAppender = method -> stringBuilder.append(method)
                        .append(" ");
        stringBuilder.append(TaskHandler.HANDLER_PATH)
                    .append(path)
                    .append(" can only handle ");
        Arrays.stream(allowedMethods)
                .forEach(methodAppender);
        stringBuilder.append("methods.");
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD, stringBuilder.toString());
    }

    public void handleInvalidRequest(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_REQUEST);
    }

    public void handleInvalidId(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_ID);
    }
}
