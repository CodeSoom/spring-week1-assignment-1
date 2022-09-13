package com.codesoom.assignment.handler;


import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private int status = 200;
    String content = "Hello World!";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream))).lines().collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        if (!body.isEmpty()) {

            Task task1 = toTask(body);
            tasks.add(task1);
        }

        getMappingTasks(method, path);


        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task";
        }

        exchange.sendResponseHeaders(status, content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private void getMappingTasks(String method, String path) throws IOException {

//        GET /tasks
        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
            return;
        }
//        GET /tasks/{id}
        else if (method.equals("GET") && path.contains("/tasks/")) {
            String[] split = path.split("/");

            String id = split[2];
            if (!isNumeric(id)) {
                System.out.println("tasks id not a number");
                status = 404;
                return;
            }

            for (Task task : tasks) {
                if (task.getId() == Long.parseLong(id)) {
                    content = taskToJson(task);
                    status = 200;
                    return;
                }
            }

            content = "";
        }


    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    private String tasksToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }


    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
