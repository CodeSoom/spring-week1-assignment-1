package com.codesoom.demo;

import com.codesoom.demo.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DemoHttpHandler implements HttpHandler {
    private static URI exchange;
    private ObjectMapper objectMapper = new ObjectMapper();


    private List<Task> tasks = new ArrayList<>();
    private Object Task;


    // 메소드를 따로 빼려고 시도한 흔적....
    public static void ExtractsId() {
        String ExtractsPath = exchange.getPath();
        int ExtractID = Integer.valueOf(ExtractsPath.substring(7));
    }

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L); // Long type 이므로 정수 뒤에 L 붙여준다.
        task.setTitle("과제하기");

        tasks.add(task);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CRUD
        // 1. method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body : content

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n")); // 여러줄을 줄넘김 하여 정렬

        System.out.println(method + " " + path);
        if (!body.isBlank()) {
            Task task = JSONTotask(body);
            tasks.add(task);
        }

        String content = "Hello, world!";


        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if (method.equals("PUT") && path.equals("/tasks/1")) {
            content = tasksToJSON();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            exchange.sendResponseHeaders(201, content.getBytes().length);
            content = tasksToJSON();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);// 길이 가져올때는 bytes 로 변환

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task JSONTotask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
