package com.codesoom.assignment;

import com.codesoom.exception.TaskNotFoundException;
import com.codesoom.http.HttpMethod;
import com.codesoom.http.HttpRequest;
import com.codesoom.http.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static com.codesoom.assignment.HttpStatus.BAD_REQUEST;
import static com.codesoom.assignment.HttpStatus.CREATED;
import static com.codesoom.assignment.HttpStatus.NOT_FOUND;
import static com.codesoom.assignment.HttpStatus.NO_CONTENT;
import static com.codesoom.assignment.HttpStatus.OK;

public class TaskHandler implements HttpHandler {
    private static final int PLACE_OF_TASK_ID_FROM_PATH = 2;

    private final TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            handleRequest(exchange);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        HttpResponse httpResponse = new HttpResponse(exchange);

        if (!HttpRequest.isValidRequest(exchange)) {
            httpResponse.response(BAD_REQUEST, "유효하지 않은 Http 요청입니다.");
            return;
        }
        HttpRequest httpRequest = new HttpRequest(exchange);

        serviceTask(httpRequest, httpResponse);
    }

    private void serviceTask(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (method.isGet() && "/tasks".equals(path)) {
            String content = taskService.getTasks();
            httpResponse.response(OK, content);
            return;
        }
        if (method.isGet() && path.startsWith("/tasks/")) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                String content = taskService.getTask(taskId);
                httpResponse.response(OK, content);
                return;
            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        }
        if (method.isPost()) {
            String content = taskService.createTask(httpRequest.getBody());
            httpResponse.response(CREATED, content);
            return;
        }
        if (method.isPut()) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                String content = taskService.updateTask(taskId, httpRequest.getBody());
                httpResponse.response(OK, content);

            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        }
        if (method.isDelete()) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                taskService.deleteTask(taskId);
                httpResponse.response(NO_CONTENT, "");
                return;
            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        }

        httpResponse.response(NOT_FOUND, "");
    }
}
