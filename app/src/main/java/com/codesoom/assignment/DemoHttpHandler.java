package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private Long defaultId = 0L;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String content = "";
        String body = getBody(exchange);

        System.out.println("method::" + requestMethod + "\npath:: " + path);
        //단일 검색
        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON(tasks);
        }
        //디테일 검색
        if (requestMethod.equals("GET") && path.startsWith("/tasks/")) {
            for (Task task1 : tasks) {
                content = compareConvert(path, content, task1);
            }
        }
        //일 생성
        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            createNewTask(body);
            content = "Create a new task";
        }
        //수정
        if (requestMethod.equals("PATCH") || requestMethod.equals("PUT") && path.startsWith("/tasks/")) {
            updateTask(path, body);
        }
        //삭제
        if (requestMethod.equals("DELETE") && path.startsWith("/tasks/")) {
            tasks.remove(getRequestId(path).intValue() - 1);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputStream(exchange, content);
    }

    private String compareConvert(String path, String content, Task task1) throws IOException {
        if (task1.getId().equals(getRequestId(path))) {
            content = tasksToJSON(task1);
        }
        return content;
    }

    private void updateTask(String path, String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(getRequestId(path));
        tasks.set(getRequestId(path).intValue() - 1, task);
    }

    private void createNewTask(String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(defaultId += 1L);
        tasks.add(task);
    }

    private void outputStream(HttpExchange exchange, String content) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getBody(HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Long getRequestId(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Task task = new Task();
        if (object.equals(tasks)) {
            objectMapper.writeValue(outputStream, this.tasks);
        } else {
            objectMapper.writeValue(outputStream, task);//?
        }
        return outputStream.toString();
    }


}