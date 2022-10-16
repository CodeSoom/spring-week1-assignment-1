package com.codesoom.assignment.Controller;

import com.codesoom.assignment.error.ClientError;
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

    public void gets(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.gets();
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(tasks));
    }

    public void get(HttpExchange exchange, long userId) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(taskByUserId.get()));
    }

    public void create(HttpExchange exchange, RequestTaskDTO.Create request) throws IOException {
        Task task = taskService.create(request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_CREATED, JsonUtil.writeValue(task));
    }

    public void update(HttpExchange exchange, long userId, RequestTaskDTO.Update request) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        Task task = taskService.update(taskByUserId.get(), request);
        setSuccessResponse(exchange, HttpURLConnection.HTTP_OK, JsonUtil.writeValue(task));
    }

    public void delete(HttpExchange exchange, long userId) throws IOException {
        Optional<Task> taskByUserId = taskService.getByUserId(userId);

        if (taskByUserId.isEmpty()) {
            ClientError.notFound(exchange);
        }

        boolean deleteResult = taskService.delete(taskByUserId.get());

        if (!deleteResult) {
            ClientError.conflict(exchange);
            return;
        }

        setSuccessResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT);
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
}
