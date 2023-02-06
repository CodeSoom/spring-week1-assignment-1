package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoHttpHadnler implements HttpHandler {
    private Long testId;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long default_id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String content = "";
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println("method::" + requestMethod + "\npath:: " + path);
        //단일 검색
        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        //디테일 검색
        //문제 해결하고
        if (requestMethod.equals("GET") && path.equals("/tasks/")) {
            Long getId = Long.parseLong(path.split("/")[2]);
            tasks.forEach(c -> {
              if (c.getId().equals(getId)){
                  System.out.println("확인용");
              }
            });
        }

        //일 생성
        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            CreateNewTask(body);
            content = "Create a new task";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void CreateNewTask(String body) throws JsonProcessingException {
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

}
