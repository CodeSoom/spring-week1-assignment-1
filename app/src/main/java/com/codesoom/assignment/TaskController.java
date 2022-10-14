package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.RequestTaskDTO;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.utils.JsonUtil;
import com.codesoom.assignment.utils.StringUtil;
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
                    return;
                }

                if (!StringUtil.isNumeric(path.getPathVariable())) {
                    ClientError.methodArgumentTypeMismatch(exchange);
                    return;
                }

                get(exchange, Long.parseLong(path.getPathVariable()));

                return;
            case POST:
                RequestTaskDTO.Create requestCreateDTO = JsonUtil.readValue(requestBody, RequestTaskDTO.Create.class);
                create(exchange, requestCreateDTO);

                return;
            case PUT:
            case PATCH:
                if (!StringUtil.isNumeric(path.getPathVariable())) {
                    ClientError.methodArgumentTypeMismatch(exchange);
                    return;
                }

                RequestTaskDTO.Update reqeustUpdateDTO = JsonUtil.readValue(requestBody, RequestTaskDTO.Update.class);
                update(exchange, Long.parseLong(path.getPathVariable()), reqeustUpdateDTO);

                return;
            case DELETE:
                if (!StringUtil.isNumeric(path.getPathVariable())) {
                    ClientError.methodArgumentTypeMismatch(exchange);
                    return;
                }

                delete(exchange, Long.parseLong(path.getPathVariable()));

                return;
            default:
                ClientError.methodNotAllowed(exchange);
        }
    }

    private void delete(HttpExchange exchange, long userId) throws IOException {
        Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        taskService.deleteTask(taskByUserId.get());
        setSuccessResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
    }

    private void update(HttpExchange exchange, long userId, RequestTaskDTO.Update request) throws IOException {
        Optional<Task> taskByUserId = taskService.getTaskByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        Task task = taskService.updateTask(taskByUserId.get(), request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(task));
    }

    private void create(HttpExchange exchange, RequestTaskDTO.Create request) throws IOException {
        Task task = taskService.createTask(request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_CREATED, JsonUtil.writeValue(task));
    }

    private void get(HttpExchange exchange, long userId) throws IOException {
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
