package com.codesoom.assignment.models;

import java.io.InputStream;
import java.net.URI;

public class HttpRequestForTasks extends HttpRequest {

    public static final String TASKS = "/tasks";
    public static final String TASKS_PATTERN = TASKS + "/*[0-9]*";

    public HttpRequestForTasks(String method, URI uri, InputStream inputStream) {
        super(method, uri, inputStream);
    }

    @Override
    public boolean isValidMethod() {
        return switch (getMethod()) {
            case GET, POST, PUT, PATCH, DELETE -> true;
            default -> false;
        };
    }

    @Override
    public boolean isValidPath() {
        String path = getPath();
        HttpRequestMethod method = getMethod();

        return switch (method) {
            case GET -> path.equals(TASKS) || path.matches(TASKS_PATTERN);
            case POST -> path.equals(TASKS);
            case PATCH, PUT, DELETE -> path.matches(TASKS_PATTERN);
            default -> false;
        };
    }

}
