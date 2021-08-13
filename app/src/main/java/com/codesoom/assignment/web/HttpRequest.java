package com.codesoom.assignment.web;

import com.codesoom.assignment.errors.MethodNotAllowedException;

public class HttpRequest {

    private static final long EMPTY_TASK_ID = 0L;

    private final Path path;
    private final AllowMethods method;

    public HttpRequest(String path, String method) {
        this.path = new Path(path);
        this.method = AllowMethods.fromString(method)
            .orElseThrow(MethodNotAllowedException::new);
    }

    public long getTaskIdFromPath() {
        String replaced = path.getTaskId();

        if (!replaced.isEmpty()) {
            return Long.parseLong(replaced);
        }

        return EMPTY_TASK_ID;
    }

    public boolean isReadAll() {
        return AllowMethods.GET.equals(method) && path.isTasks();
    }

    public boolean isReadOne() {
        return AllowMethods.GET.equals(method) && path.hasTaskId();
    }

    public boolean isCreateOne() {
        return AllowMethods.POST.equals(method) && path.isTasks();
    }

    public boolean isUpdateOne() {
        return (AllowMethods.PUT.equals(method) || AllowMethods.PATCH.equals(method))
            && path.hasTaskId();
    }

    public boolean isDeleteOne() {
        return AllowMethods.DELETE.equals(method) && path.hasTaskId();
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }
}
