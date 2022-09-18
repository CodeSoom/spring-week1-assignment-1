package com.codesoom.assignment;

import com.codesoom.exception.MethodNotExistException;
import com.codesoom.exception.TaskNotFoundException;
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
            String content = getTasks();
            httpResponse.response(OK, content);
            return;
        } else if (method.isGet() && path.startsWith("/tasks/")) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                String content = getTask(taskId); // 서비스 (비즈니스 로직)
                httpResponse.response(OK, content);
                return;
            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        } else if (method.isPost()) {
            String content = createTask(httpRequest.getBody());
            httpResponse.response(CREATED, content);
            return;
        } else if (method.isPut()) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                String content = updateTask(taskId, httpRequest.getBody());
                httpResponse.response(OK, content);

            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        } else if (method.isDelete()) {
            try {
                Long taskId = httpRequest.getLongFromPathParameter(PLACE_OF_TASK_ID_FROM_PATH);
                deleteTask(taskId);
                httpResponse.response(NO_CONTENT, "");
                return;
            } catch (TaskNotFoundException e) {
                httpResponse.response(NOT_FOUND, "");
                return;
            }
        }

        httpResponse.response(NOT_FOUND, "");
    }

    private String getTasks() throws IOException {
        List<Task> tasks = taskRepository.findAll();
        return JsonParser.objectToJson(tasks);
    }

    private String getTask(Long id) throws IOException {
        if (taskRepository.isExist(id)) {
            Task task = taskRepository.findById(id);
            return JsonParser.objectToJson(task);
        }
        throw new TaskNotFoundException();
    }

    private String createTask(String body) throws IOException {
        Task task = JsonParser.requestBodyToObject(body, Task.class);
        Task savedTask = taskRepository.save(task);

        return JsonParser.objectToJson(savedTask);
    }

    private String updateTask(Long id, String body) throws IOException {
        if (taskRepository.isExist(id)) {
            Task task = JsonParser.requestBodyToObject(body, Task.class);
            task.setId(id);

            Task updateTask = taskRepository.update(task);

            return JsonParser.objectToJson(updateTask);
        }
        throw new TaskNotFoundException();
    }

    private void deleteTask(Long id) {
        if (taskRepository.isExist(id)) {
            taskRepository.delete(id);
            return;
        }
        throw new TaskNotFoundException();
    }
}
