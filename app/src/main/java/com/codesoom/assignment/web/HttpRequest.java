package com.codesoom.assignment.web;

import com.codesoom.assignment.TaskManager;
import com.codesoom.assignment.errors.MethodNotAllowedException;
import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

    private static final long EMPTY_TASK_ID = 0L;
    private static final String NOT_FOUND_MESSAGE = "Not Found.";

    private final TaskManager taskManager = TaskManager.getInstance();

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

    public HttpResponse getHttpResponse(HttpExchange httpExchange) throws IOException {
        String body = getParsedResponseBody(httpExchange);

        long taskId = getTaskIdFromPath();
        if (isReadAll()) {
            return new HttpResponseOK(httpExchange, taskManager.getAllTasks());
        }

        if (isReadOne()) {
            try {
                return new HttpResponseOK(httpExchange, taskManager.findTaskWith(taskId));
            } catch (TaskIdNotFoundException error) {
                return new HttpResponseNotFound(httpExchange, error.getMessage());
            }
        }

        if (isCreateOne()) {
            if (body.isEmpty()) {
                return new HttpResponseNoContent(httpExchange);
            }

            String createdTask = taskManager.createTask(body);

            return new HttpResponseCreated(httpExchange, createdTask);
        }

        if (isUpdateOne()) {
            try {
                String updatedTask = taskManager.updateTask(taskId, body);

                return new HttpResponseOK(httpExchange, updatedTask);
            } catch (TaskIdNotFoundException error) {
                return new HttpResponseNotFound(httpExchange, error.getMessage());
            }
        }

        if (isDeleteOne()) {
            try {
                taskManager.deleteTask(taskId);

                return new HttpResponseNoContent(httpExchange);
            } catch (TaskIdNotFoundException error) {
                return new HttpResponseNotFound(httpExchange, error.getMessage());
            }
        }

        return new HttpResponseNotFound(httpExchange, NOT_FOUND_MESSAGE);
    }

    private boolean isReadAll() {
        return AllowMethods.GET.equals(method) && path.isTasks();
    }

    private boolean isReadOne() {
        return AllowMethods.GET.equals(method) && path.hasTaskId();
    }

    private boolean isCreateOne() {
        return AllowMethods.POST.equals(method) && path.isTasks();
    }

    private boolean isUpdateOne() {
        return (AllowMethods.PUT.equals(method) || AllowMethods.PATCH.equals(method))
            && path.hasTaskId();
    }

    private boolean isDeleteOne() {
        return AllowMethods.DELETE.equals(method) && path.hasTaskId();
    }

    private String getParsedResponseBody(HttpExchange httpExchange) {
        InputStream httpRequestBody = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }

}
