package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private final List<Map<String, Task>> tasks = new ArrayList<>();
    private final Map<String, Task> taskMap = new HashMap<>();
    private final JsonConverter jsonConverter = new JsonConverter();
    private static Long sequence = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final String body = createBody(exchange);

        HttpRequest httpRequest = new HttpRequest(method, path);
        System.out.println(method + " " + path);

        String id = checkPathGetId(path);
        String content = "";

        int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.getCode();

        // GET /tasks
        if(httpRequest.isGetAllTasks()) {
            content = jsonConverter.tasksToJSON(tasks);
            httpStatusCode = HttpStatus.OK.getCode();
        }

        // GET /tasks/{id}
        if(httpRequest.isGetOneTask(method, path)) {
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                content =  jsonConverter.oneTaskToJSON(task.get());
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // POST /tasks
        if(httpRequest.isCreateTask(method, path)){
            createTask(body);
            content = jsonConverter.tasksToJSON(tasks);
            httpStatusCode = HttpStatus.CREATED.getCode();
        }

        // PUT,PATCH /tasks/{id}
        if(httpRequest.isUpdateTask(method, path)) {
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                Task updateTask = updateTask(task.get(), body);
                content =  jsonConverter.oneTaskToJSON(updateTask);
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // Delete /tasks/{id}
        if(httpRequest.isDeleteTask(method, path)) {
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                deleteTask(id);
                httpStatusCode = HttpStatus.NO_CONTENT.getCode();
            }
        }

        exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);

        OutputStream outputstream = exchange.getResponseBody();
        outputstream.write(content.getBytes());
        outputstream.flush();
        outputstream.close();
    }

    private String createBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return body;
    }

    private String checkPathGetId(String path) {
        if (path.indexOf("/tasks/") == 0) {
            return path.substring(7);
        }
        return "";
    }

    private void deleteTask(String id) {
        tasks.remove(id);
    }

    private Task updateTask(Task task, String content) throws JsonProcessingException {
        Task originTask = jsonConverter.jsonToTask(content);
        task.setTitle(originTask.getTitle());
        return task;
    }

    private Optional findId(String id) {
        Optional<Task> task = Optional.empty();
        Task findTask = taskMap.get(id);
        if (findTask == null) {
            return task;
        }
        return task.of(findTask);
    }

    private void createTask(String body) throws JsonProcessingException {
        Task task = jsonConverter.jsonToTask(body);
        task.setId(++sequence);
        taskMap.put(task.getId() + "", task);
        tasks.add(taskMap);
    }
}
