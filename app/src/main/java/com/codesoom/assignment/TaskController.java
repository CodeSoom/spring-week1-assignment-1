package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.utils.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;

public class TaskController {
    private static final TaskController taskController = new TaskController();
    private final TaskService taskService = TaskService.getInstance();

    private TaskController() {
    }

    public static TaskController getInstance() {
        return taskController;
    }

    public void execute(HttpExchange exchange, HttpMethodType method, Path path, String requestBody) throws IOException {

        if (isRequiredPathVariable(method) && path.getPathVariable() == null) {
            ClientError.badRequest(exchange);
            return;
        }

        if (isRequiredRequestBody(method) && requestBody.isBlank()) {
            ClientError.badRequest(exchange);
            return;
        }

        switch (method) {
            case GET:
                if (path.getPathVariable() == null) {
                    gets(exchange);
                } else {
                    get(exchange, path);
                }
                break;
            case POST:
                create(exchange, requestBody);
                break;
            case PUT:
            case PATCH:
                update(exchange, path, requestBody);
                break;
            case DELETE:
                delete(exchange, path);
                break;
            default:
                ClientError.methodNotAllowed(exchange);
        }
    }

    private void delete(HttpExchange exchange, Path path) throws IOException {
        long userId = Long.parseLong(path.getPathVariable());
        Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        taskService.deleteTask(taskByUserId.get());
        setSuccessResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
    }

    private void update(HttpExchange exchange, Path path, String requestBody) throws IOException {
        long userId = Long.parseLong(path.getPathVariable());
        Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        Task task = taskService.updateTask(taskByUserId.get(), requestBody);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(task));
    }

    private void create(HttpExchange exchange, String requestBody) throws IOException {
        Task task = taskService.createTask(requestBody);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_CREATED, JsonUtil.writeValue(task));
    }

    private void get(HttpExchange exchange, Path path) throws IOException {
        long userId = Long.parseLong(path.getPathVariable());
        Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(taskByUserId.get()));
    }

    private void gets(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.getTasks();
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(tasks));
    }

    private void setSuccessResponse(HttpExchange exchange, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        exchange.getResponseBody().close();
    }

    private void setSuccessResponse(HttpExchange exchange, int responseCode, String data) throws IOException {
        exchange.sendResponseHeaders(responseCode, data.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(data.getBytes());
        responseBody.flush();
        responseBody.close();
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
