// TODO
// 1. Read - collection (완)
// 2. Read - item/element
// 3. Create (완)
// 4. Update
// 5. Delete

package com.assignment1.demo;

import com.assignment1.demo.models.Task;
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
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing");
        tasks.add(task);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String method = exchange.getRequestMethod();

        // 2. path 확인하기
        URI uri = exchange.getRequestURI();
        // path string만 얻기
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);
        if (!body.isBlank()) {
            System.out.println(body);
            Task task = toTask(body);
            System.out.println(task);
        }
        System.out.println(body);

        String content = "REST API practice...";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = toJSON();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task!";
            exchange.sendResponseHeaders(201, content.getBytes().length);
        }


        // http status code, content 길이 반환
        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        // write 메서드가 byte[]를 받으므로, content 넘겨줄 때 getBytes 메소드 사용.
        outputStream.write(content.getBytes());

        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
