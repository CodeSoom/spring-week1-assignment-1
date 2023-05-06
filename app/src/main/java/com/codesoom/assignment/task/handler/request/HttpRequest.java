package com.codesoom.assignment.task.handler.request;

import com.codesoom.assignment.task.exception.InvalidValueException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

    private final HttpExchange httpExchange;

    public HttpRequest(final HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public String getMethod() {
        return httpExchange.getRequestMethod();
    }

    public String getPath() {
        return httpExchange.getRequestURI().getPath();
    }

    public String getBody() {
        return new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public long parseIdFromPath() {
        String[] pathElements = getPath().split("/");
        try {
            return Long.parseLong(pathElements[pathElements.length - 1]);
        } catch (InvalidValueException e) {
            throw new InvalidValueException(String.format("Invalid Task ID format in the path: %s", getPath()));
        }
    }

}
