package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHandler implements HttpHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    TaskList taskList = new TaskList();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String content = "";
        int httpCode = 200;
        String requestMethod = exchange.getRequestMethod();
        String[] pathArray = createPathArray(exchange);

        String requestJsonTitle = createBody(exchange);

        if (pathArray.length >= 2 && pathArray[1].equals("tasks")) {
            if (requestMethod.equals("GET")) {
                if (pathArray.length == 2) {
                    content = createTasksListResponse();
                } else if (pathArray.length == 3) {
                    int requestTaskId = getRequestTaskId(pathArray);
                    if (requestTaskId >= 1 && requestTaskId <= taskList.size()) {
                        Task task = taskList.get(requestTaskId);
                        if (task == null) {
                            httpCode = 404;
                        } else {
                            content = taskToJson(task);
                        }
                    } else {
                        httpCode = 404;
                    }
                }
            } else if (requestMethod.equals("POST")) {
                if (!requestJsonTitle.isBlank()) {
                    Task task = addTask(requestJsonTitle);
                    httpCode = 201;
                    content = taskToJson(task);
                }
            } else if (pathArray.length == 3 && requestMethod.equals("PUT")) {

                int requestTaskId = getRequestTaskId(pathArray);
                Task requestTask = toTask(requestJsonTitle);
                taskList.updateTask(requestTaskId, requestTask);
                content = taskToJson(taskList.get(requestTaskId));


            } else if (pathArray.length == 3 && requestMethod.equals("DELETE")) {
                int requestTaskId = getRequestTaskId(pathArray);
                if (taskList.delete(requestTaskId)) {
                    httpCode = 200;
                } else {
                    httpCode = 404;
                }
            }
        }

        exchange.sendResponseHeaders(httpCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String createTasksListResponse() throws IOException {
        String content;
        if (taskList.size() == 0) {
            content = "[]";
        } else {
            content = taskToJson();
        }
        return content;
    }


    private int getRequestTaskId(String[] pathArray) {
        String taskIdStr = pathArray[2];
        return Integer.parseInt(taskIdStr);
    }

    private String[] createPathArray(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        return path.split("/");
    }

    private static String createBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    private String taskToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskList.getItems());
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Task addTask(String requestJsonTitle) throws JsonProcessingException {
        Task task = toTask(requestJsonTitle);
        taskList.add(task);
        return task;
    }
}
