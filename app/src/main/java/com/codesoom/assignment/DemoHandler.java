package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHandler implements HttpHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    TaskList taskList = new TaskList();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String content = "test";

        String requestMethod = exchange.getRequestMethod();
        String[] pathArray = createPathArray(exchange);

        String requestJsonTitle = createBody(exchange);

        if (pathArray.length >= 2 && pathArray[1].equals("tasks")) {
            if (pathArray.length == 2 && requestMethod.equals("GET")) {
                if (taskList.size() == 0) {
                    content = "[]";
                } else {
                    content = taskToJson();
                }

            } else if (requestMethod.equals("POST")) {
                if (!requestJsonTitle.isBlank()) {
                    taskList.add(toTask(requestJsonTitle));
                }
                content = taskToJson();

            } else if (pathArray.length == 3 && requestMethod.equals("PUT")) {

                String taskIdStr = pathArray[2];
                int requestTaskId = Integer.parseInt(taskIdStr);
                Task requestTask = toTask(requestJsonTitle);

                for (int i = 0; i < taskList.size(); i++) {
                    if (hasTaskThenUpdateTitle(requestTask.getTitle(), requestTaskId, i)) {
                        content = taskToJson(taskList.get(i));
                        break;
                    }
                }
            }
        }


        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
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

    private boolean hasTaskThenUpdateTitle(String requestTitle, int requestTaskId, int i) {
        Task task = taskList.get(i);
        if (task.isTaskId(requestTaskId)) {
            task.updateTitle(requestTitle);
            return true;
        }
        return false;
    }


    private Task toTask(String body) throws JsonProcessingException {
        Task task = objectMapper.readValue(body, Task.class);
        return task;
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
}
