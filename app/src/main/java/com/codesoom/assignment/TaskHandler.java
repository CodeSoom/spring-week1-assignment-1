package com.codesoom.assignment;

import com.codesoom.controller.HttpMethod;
import com.codesoom.http.HttpRequest;
import com.codesoom.http.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

import static com.codesoom.assignment.HttpStatus.BAD_REQUEST;
import static com.codesoom.assignment.HttpStatus.CREATED;
import static com.codesoom.assignment.HttpStatus.NOT_FOUND;
import static com.codesoom.assignment.HttpStatus.NO_CONTENT;
import static com.codesoom.assignment.HttpStatus.OK;

public class TaskHandler implements HttpHandler {
    private static final int PLACE_OF_TASK_ID_FROM_PATH = 2;

    private final TaskRepository taskRepository = new TaskRepository();

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
        HttpRequest httpRequest = new HttpRequest(exchange);
        HttpResponse httpResponse = new HttpResponse(exchange);

        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();
        String body = httpRequest.getBody();

        HttpStatus status = OK;
        String content = "";

        if (method.isGet() && "/tasks".equals(path)) {
            List<Task> tasks = taskRepository.findAll();

            content = JsonParser.objectToJson(tasks);
            httpResponse.response(OK, content);
            return;

        } else if (method.isGet() && path.startsWith("/tasks/")) {
            Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
            if (id == null) {
                httpResponse.response(BAD_REQUEST, content);
            }

            Task task = taskRepository.findById(id);
            if (task == null) {
                status = NOT_FOUND;
            } else {
                content = JsonParser.objectToJson(task);
            }
            httpResponse.response(status, content);
            return;

        } else if (method.isPost()) {
            Task task = JsonParser.requestBodyToObject(body, Task.class);
            Task savedTask = taskRepository.save(task);

            content = JsonParser.objectToJson(savedTask);
            httpResponse.response(CREATED, content);
            return;

        } else if (method.isPut()) {
            Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
            if (id == null) {
                httpResponse.response(BAD_REQUEST, content);
            }

            if (taskRepository.findById(id) == null) {
                status = NOT_FOUND;
            } else {
                Task task = JsonParser.requestBodyToObject(body, Task.class);
                task.setId(id);

                Task updateTask = taskRepository.update(task);

                content = JsonParser.objectToJson(updateTask);

            }
            httpResponse.response(status, content);
            return;


        } else if (method.isDelete()) {
            Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
            if (id == null) {
                httpResponse.response(BAD_REQUEST, content);
            }

            if (taskRepository.findById(id) == null) {
                status = NOT_FOUND;
            } else {
                taskRepository.delete(id);
                status = NO_CONTENT;
            }
            httpResponse.response(status, content);
            return;
        }

        // 404
        httpResponse.response(NOT_FOUND, content);
    }
}
