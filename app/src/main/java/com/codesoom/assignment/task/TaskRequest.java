package com.codesoom.assignment.task;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.exception.HttpBadRequestException;
import com.codesoom.assignment.http.exception.HttpNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.http.HttpMethod.*;

public class TaskRequest {

    private static final String RESOURCE_NAME = "tasks";

    private static final int RESOURCE_POSITION = 1;

    private static final int RESOURCE_ID_POSITION = 2;

    private static final int HAS_ID_PATHS_LENGTH = 3;

    private static final List<Integer> ALLOW_PATH_LENGTH = Arrays.asList(2, 3);

    private final TaskRequestType taskRequestType;

    private final String[] pathSegments;

    public TaskRequest(final HttpExchange exchange) {
        this.pathSegments = exchange.getRequestURI().getPath().split("/");

        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        this.taskRequestType = getRequestType(httpMethod);

        validatePath();
    }

    private void validatePath() {

        if (!pathSegments[RESOURCE_POSITION].equals(RESOURCE_NAME)) {
            throw new HttpNotFoundException();
        }

        if (!ALLOW_PATH_LENGTH.contains(pathSegments.length)) {
            throw new HttpBadRequestException();
        }
    }

    public Long getId() {
        return Long.valueOf(pathSegments[RESOURCE_ID_POSITION]);
    }

    public String getRequestBody(final HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (requestBody.isEmpty()) {
            throw new HttpBadRequestException();
        }
        return requestBody;
    }

    public boolean is(final TaskRequestType taskRequestType) {
        return this.taskRequestType.equals(taskRequestType);
    }

    private TaskRequestType getRequestType(HttpMethod httpMethod) {
        if (httpMethod.equals(GET)) {
            if (hasResourceId()) {
                return TaskRequestType.VIEW;
            }
            return TaskRequestType.LIST;
        }

        if (httpMethod.equals(PUT) && hasResourceId()) {
            return TaskRequestType.REPLACE;
        }

        if (httpMethod.equals(PATCH) && hasResourceId()) {
            return TaskRequestType.MODIFY;
        }

        if (httpMethod.equals(DELETE) && hasResourceId()) {
            return TaskRequestType.DELETE;
        }

        if (httpMethod.equals(POST) && hasNotResourceId()) {
            return TaskRequestType.SAVE;
        }
        throw new HttpBadRequestException();
    }

    private boolean hasResourceId() {
        return pathSegments.length == HAS_ID_PATHS_LENGTH;
    }

    private boolean hasNotResourceId() {
        return !hasResourceId();
    }
}
