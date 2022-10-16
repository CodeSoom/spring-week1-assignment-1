package com.codesoom.assignment;

import com.codesoom.assignment.Controller.TaskController;
import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.RequestTaskDTO;
import com.codesoom.assignment.utils.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class TaskControllerAdvice {
    private static final TaskControllerAdvice taskControllerAdvice = new TaskControllerAdvice();
    private final TaskController taskController = TaskController.getInstance();

    private TaskControllerAdvice() {
    }

    public static TaskControllerAdvice getInstance() {
        return taskControllerAdvice;
    }

    public void requestMapping(HttpExchange exchange,
                               HttpMethodType method,
                               Path path,
                               String requestBody) throws IOException {
        if (isRequiredPathVariable(method) && isEmptyPathVariable(path)) {
            ClientError.badRequest(exchange);
            return;
        }

        if (isRequiredRequestBody(method) && requestBody.isBlank()) {
            ClientError.badRequest(exchange);
            return;
        }

        switch (method) {
            case GET:
                responseTaskAdvice(exchange, path);
                return;
            case POST:
                createTaskAdvice(exchange, requestBody);
                return;
            case PUT:
            case PATCH:
                updateTaskAdvice(exchange, path, requestBody);
                return;
            case DELETE:
                deleteTaskAdvice(exchange, path);
                return;
            default:
                ClientError.methodNotAllowed(exchange);
        }
    }

    private void deleteTaskAdvice(HttpExchange exchange, Path path) throws IOException {
        if (!path.isInvalidPathVariable()) {
            ClientError.methodArgumentTypeMismatch(exchange);
            return;
        }

        taskController.delete(exchange, Long.parseLong(path.getPathVariable()));
    }

    private void updateTaskAdvice(HttpExchange exchange, Path path, String requestBody) throws IOException {
        if (!path.isInvalidPathVariable()) {
            ClientError.methodArgumentTypeMismatch(exchange);
            return;
        }

        RequestTaskDTO.Update request = JsonUtil.readValue(requestBody, RequestTaskDTO.Update.class);

        taskController.update(exchange, Long.parseLong(path.getPathVariable()), request);
    }

    private void createTaskAdvice(HttpExchange exchange, String requestBody) throws IOException {
        RequestTaskDTO.Create request = JsonUtil.readValue(requestBody, RequestTaskDTO.Create.class);
        taskController.create(exchange, request);
    }

    private void responseTaskAdvice(HttpExchange exchange, Path path) throws IOException {
        if (isEmptyPathVariable(path)) {
            taskController.gets(exchange);
            return;
        }

        if (!path.isInvalidPathVariable()) {
            ClientError.methodArgumentTypeMismatch(exchange);
            return;
        }

        taskController.get(exchange, Long.parseLong(path.getPathVariable()));
    }

    private boolean isEmptyPathVariable(Path path) {
        return path.getPathVariable() == null;
    }

    private boolean isRequiredRequestBody(HttpMethodType method) {
        return method == HttpMethodType.POST
                || method == HttpMethodType.PUT
                || method == HttpMethodType.PATCH;
    }

    private boolean isRequiredPathVariable(HttpMethodType method) {
        return method == HttpMethodType.PUT
                || method == HttpMethodType.PATCH
                || method == HttpMethodType.DELETE;
    }
}
