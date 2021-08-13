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

    private final JsonConverter jsonConverter = new JsonConverter();
    private final TaskFactory taskFactory = new TaskFactory();

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
            List<Map<String, Task>> tasks = taskFactory.getTasks();
            content = jsonConverter.tasksToJSON(tasks);
            httpStatusCode = HttpStatus.OK.getCode();
        }

        // GET /tasks/{id}
        if(httpRequest.isGetOneTask()) {
            Optional<Task> task = taskFactory.findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                content =  jsonConverter.oneTaskToJSON(task.get());
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // POST /tasks
        if(httpRequest.isCreateTask()){
            taskFactory.createTask(body);
            List<Map<String, Task>> tasks = taskFactory.getTasks();
            content = jsonConverter.tasksToJSON(tasks);
            httpStatusCode = HttpStatus.CREATED.getCode();
        }

        // PUT,PATCH /tasks/{id}
        if(httpRequest.isUpdateTask()) {
            Optional<Task> task = taskFactory.findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                Task updateTask = taskFactory.updateTask(task.get(), body);
                content =  jsonConverter.oneTaskToJSON(updateTask);
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // Delete /tasks/{id}
        if(httpRequest.isDeleteTask()) {
            Optional<Task> task = taskFactory.findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                taskFactory.deleteTask(id);
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

}
