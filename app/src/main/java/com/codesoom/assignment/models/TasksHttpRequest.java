package com.codesoom.assignment.models;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

public class TasksHttpRequest extends HttpRequest {

    public static final String TASKS = "/tasks";
    public static final String TASKS_PATTERN = TASKS + "/*[0-9]*";

    public TasksHttpRequest(String method, URI uri, InputStream inputStream) {
        super(method, uri, inputStream);
    }

    @Override
    public boolean isValidMethod() {
        return Arrays.stream(TasksHttpRequestMethod.values()).anyMatch(method -> method == getMethod());
    }

    @Override
    public boolean isValidPath() {
        String path = getPath();
        TasksHttpRequestMethod method = getMethod();

        return switch (method) {
            case GET -> path.equals(TASKS) || path.matches(TASKS_PATTERN);
            case POST -> path.equals(TASKS);
            case PATCH, PUT, DELETE -> path.matches(TASKS_PATTERN);
            default -> false;
        };
    }

}
