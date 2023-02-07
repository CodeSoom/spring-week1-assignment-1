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
    private Long default_id = 0L;

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
            content = tasksToJSON();
        }

        //디테일 검색
        if (requestMethod.equals("GET") && path.startsWith("/tasks/")) {
            Task task = new Task();
            Long getId = gerRequestId(path);
            task.setId(getId);

//            tasks.forEach(c -> {
//                if (c.getId().equals(getId)) {
//                    try {
//                        String toJSON = tasksToJSON(task);
//                        content = toJSON;
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });

            for (Task task1 : tasks) {
                if(task1.getId().equals(getId)){
                    content = tasksToJSON(task1);
                }
            }
        }

        //일 생성
        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            createNewTask(body);
            content = "Create a new task";
        }

        if (requestMethod.equals("PATCH") && path.equals("/tasks")) {

        }
        if (requestMethod.equals("DELETE") && path.equals("/tasks")) {

        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputStream(exchange, content);
    }

    private  void outputStream(HttpExchange exchange, String content) throws IOException {
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

    private Long gerRequestId(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

    private void createNewTask(String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(default_id += 1L);
        tasks.add(task);
    }


    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String tasksToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }


}