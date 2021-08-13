package com.codesoom.assignment.web;

import com.codesoom.assignment.errors.MethodNotAllowedException;
import java.util.regex.Pattern;

public class HttpRequest {

    private static final String PREFIX_PATH = "/tasks";
    private static final long EMPTY_TASK_ID = 0L;

    private final String path;
    private final AllowMethods method;

    public HttpRequest(String path, String method) {
        this.path = path;
        this.method = AllowMethods.fromString(method)
            .orElseThrow(MethodNotAllowedException::new);
    }

    public long getTaskIdFromPath() {
        String replaced = path.replace(PREFIX_PATH, "")
            .replace("/", "");

        if (!replaced.isEmpty()) {
            return Long.parseLong(replaced);
        }

        return EMPTY_TASK_ID;
    }

    public boolean isReadAll() {
        return AllowMethods.GET.equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isReadOne() {
        return AllowMethods.GET.equals(method) && hasTaskId();
    }

    public boolean isCreateOne() {
        return AllowMethods.POST.equals(method) && PREFIX_PATH.equals(path);
    }

    public boolean isUpdateOne() {
        return (AllowMethods.PUT.equals(method) || AllowMethods.PATCH.equals(method))
            && hasTaskId();
    }

    public boolean isDeleteOne() {
        return AllowMethods.DELETE.equals(method) && hasTaskId();
    }

    private boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }
}
