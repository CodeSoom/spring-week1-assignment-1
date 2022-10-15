package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.RequestTaskDTO;
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

        deleteTask(exchange, Long.parseLong(path.getPathVariable()));
    }

    private void updateTaskAdvice(HttpExchange exchange, Path path, String requestBody) throws IOException {
        if (!path.isInvalidPathVariable()) {
            ClientError.methodArgumentTypeMismatch(exchange);
            return;
        }

        RequestTaskDTO.Update reqeustUpdateDTO = JsonUtil.readValue(requestBody, RequestTaskDTO.Update.class);
        updateTask(exchange, Long.parseLong(path.getPathVariable()), reqeustUpdateDTO);
    }

    private void createTaskAdvice(HttpExchange exchange, String requestBody) throws IOException {
        RequestTaskDTO.Create requestCreateDTO = JsonUtil.readValue(requestBody, RequestTaskDTO.Create.class);
        createTask(exchange, requestCreateDTO);
    }

    private void responseTaskAdvice(HttpExchange exchange, Path path) throws IOException {
        if (isEmptyPathVariable(path)) {
            getTaskList(exchange);
            return;
        }

        if (!path.isInvalidPathVariable()) {
            ClientError.methodArgumentTypeMismatch(exchange);
            return;
        }

        getTask(exchange, Long.parseLong(path.getPathVariable()));
    }

    private boolean isEmptyPathVariable(Path path) {
        return path.getPathVariable() == null;
    }

    private void deleteTask(HttpExchange exchange, long userId) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        taskService.delete(taskByUserId.get());
        setSuccessResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
    }

    private void updateTask(HttpExchange exchange, long userId, RequestTaskDTO.Update request) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        Task task = taskService.update(taskByUserId.get(), request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(task));
    }

    private void createTask(HttpExchange exchange, RequestTaskDTO.Create request) throws IOException {
        Task task = taskService.create(request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_CREATED, JsonUtil.writeValue(task));
    }

    private void getTask(HttpExchange exchange, long userId) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(taskByUserId.get()));
    }

    private void getTaskList(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.gets();
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
