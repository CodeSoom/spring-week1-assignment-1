package com.codesoom.assignment;

import com.codesoom.exception.MethodNotExistException;
import com.codesoom.http.HttpMethod;
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
        HttpResponse httpResponse = new HttpResponse(exchange);
        HttpRequest httpRequest;
        // todo Exception을 어디서 처리해야할지 고민된다.
        try {
            httpRequest = new HttpRequest(exchange);
        } catch (MethodNotExistException e) {
            httpResponse.response(BAD_REQUEST, e.getMessage());
            return;
        }

        HttpMethod method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        if (method.isGet() && "/tasks".equals(path)) {
            list(httpResponse);
            return;
        } else if (method.isGet() && path.startsWith("/tasks/")) {
            retrieve(httpRequest, httpResponse);
            return;
        } else if (method.isPost()) {
            post(httpRequest, httpResponse);
            return;
        } else if (method.isPut()) {
            put(httpRequest, httpResponse);
            return;

        } else if (method.isDelete()) {
            delete(httpRequest, httpResponse);
            return;
        }

        httpResponse.response(NOT_FOUND, "");
    }

    private void list(HttpResponse httpResponse) throws IOException {
        List<Task> tasks = taskRepository.findAll();
        String content = JsonParser.objectToJson(tasks);
        httpResponse.response(OK, content);
    }

    private void retrieve(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);

        if (taskRepository.isExist(id)) {
            Task task = taskRepository.findById(id);
            httpResponse.response(OK, JsonParser.objectToJson(task));

            return;
        }
        httpResponse.response(NOT_FOUND, "");

    }

    private void post(HttpRequest request, HttpResponse httpResponse) throws IOException {
        String body = request.getBody();
        Task task = JsonParser.requestBodyToObject(body, Task.class);
        Task savedTask = taskRepository.save(task);

        String content = JsonParser.objectToJson(savedTask);
        httpResponse.response(CREATED, content);
    }

    private void put(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);

        if (taskRepository.isExist(id)) {
            String body = httpRequest.getBody();
            Task task = JsonParser.requestBodyToObject(body, Task.class);
            task.setId(id);

            Task updateTask = taskRepository.update(task);

            String content = JsonParser.objectToJson(updateTask);
            httpResponse.response(OK, content);

            return;
        }
        httpResponse.response(NOT_FOUND, "");
    }

    private void delete(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Long id = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);

        if (taskRepository.isExist(id)) {
            taskRepository.delete(id);
            httpResponse.response(NO_CONTENT, "");

            return;
        }
        httpResponse.response(NOT_FOUND, "");
    }
}
