package com.codesoom.assignment.web;

import com.codesoom.assignment.errors.NotAllowedMethodException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class HttpRequest {

    public static final String PREFIX_PATH = "/tasks";

    private final String path;
    private final String method;

    private enum AllowMethods {
        GET, POST, PUT, PATCH, DELETE;

        public static boolean exist(String requestMethod) {
            return Arrays.stream(AllowMethods.values())
                .anyMatch(allowMethod -> allowMethod.toString().equals(requestMethod));
        }
    }

    public HttpRequest(String path, String method) {
        checkAllowMethod(method);
        this.path = path;
        this.method = method;
    }

    private void checkAllowMethod(String requestMethod) {
        if (!AllowMethods.exist(requestMethod)) {
            throw new NotAllowedMethodException(requestMethod);
        }
    }

    public Long getTaskIdFromPath() {
        String replaced = path.replace(PREFIX_PATH, "")
            .replace("/", "");

        return Long.parseLong(replaced);
    }

    public boolean isReadAll() {
        return "GET".equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isReadOne() {
        return "GET".equals(method) && hasTaskId();
    }

    public boolean isCreateOne() {
        return "POST".equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isUpdateOne() {
        return ("PUT".equals(method) || "PATCH".equals(method))
            && hasTaskId();
    }

    public boolean isDeleteOne() {
        return "DELETE".equals(method) && hasTaskId();
    }

    private boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }
}
