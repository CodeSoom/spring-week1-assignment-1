package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpMethod;
import com.codesoom.assignment.models.StatusCode;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.codesoom.assignment.utils.TodoHttpHandlerUtils.*;

public class TodoHttpHandler implements HttpHandler {
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    private static final String URI_WITHOUT_PARAMETERS = "/tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "";

        if (HttpMethod.GET.getHttpMethod().equals(method)) {
            handleBasicGetMethod(path, content, exchange);
        }
        if (HttpMethod.POST.getHttpMethod().equals(method)) {
            handleGetMethodWithParameter(content, exchange, body);
        }
        if (HttpMethod.PUT.getHttpMethod().equals(method)) {
            handlePutMethod(path, content, exchange, body);
        }
        if (HttpMethod.PATCH.getHttpMethod().equals(method)) {
            handlePatchMethod(path, content, exchange, body);
        }
        if (HttpMethod.DELETE.getHttpMethod().equals(method)) {
            handleDeleteMethod(path, content, exchange);
        }
    }

    public void handleDeleteMethod(String path, String content, HttpExchange exchange) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);
        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }

        taskMap.remove(id);
        content = "";
        writeOutputStream(exchange, content, StatusCode.NO_CONTENT);
    }

    public void handlePatchMethod(String path, String content, HttpExchange exchange, String body) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);
        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }
        Title title = toTitle(body);
        task.setTitle(title.getTitle());
        taskMap.put(id, task);

        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }

    public void handlePutMethod(String path, String content, HttpExchange exchange, String body) throws IOException {
        Long id = getId(path);
        Task task = taskMap.get(id);
        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }

        Task changeTask = toTask(body);
        task.setTitle(changeTask.getTitle());
        taskMap.put(id, task);

        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }

    public void handleGetMethodWithParameter(String content, HttpExchange exchange, String body) throws IOException {
        Task task = toTask(body);
        taskMap.put(task.getId(), task);

        Long lastSequence = Task.getSequence();
        Task lastTask = taskMap.get(lastSequence);

        content = taskToJSON(lastTask);
        writeOutputStream(exchange, content, StatusCode.Created);
    }

    public void handleBasicGetMethod(String path, String content, HttpExchange exchange) throws IOException {
        if (URI_WITHOUT_PARAMETERS.equals(path)) {
            content = tasksToJSON(taskMap);
            writeOutputStream(exchange, content, StatusCode.OK);
            return;
        }
        Long id = getId(path);
        Task task = taskMap.get(id);
        if (task == null) {
            writeOutputStream(exchange, content, StatusCode.NOT_FOUND);
            return;
        }
        content = taskToJSON(task);
        writeOutputStream(exchange, content, StatusCode.OK);
    }
}
