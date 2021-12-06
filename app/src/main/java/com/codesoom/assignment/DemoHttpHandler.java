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
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();


    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CURD
        // 1. Method - Get, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        //변하지 않는 값
        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();

        System.out.println(method + " " + path);


        if(path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if(path.startsWith("/tasks/")) { //startWith에 대해 공부
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            handleItem(exchange, method, id);
            return;
        }

        send(exchange, 200, "Hello, world!");
    }

    private void handleCollection(HttpExchange exchange, String method)
            throws IOException {
        if (method.equals("GET") ) {
            handleList(exchange);
        }

        if (method.equals("POST")) {
            handleCreate(exchange);
        }
    }

    private void handleItem(HttpExchange exchange, String method, Long id)
            throws IOException {
        Task task = findTask(id);

        if(task == null){ //값이 없을때 404코드로 리턴한다.
            send(exchange, 404, "");
            return;
        }

        if (method.equals("GET") ) {
            handleDetail(exchange, task);
        }

        if (method.equals("PUT") || method.equals("PATH")) {//PUT : 전체 PATH : 일부분
            handleUpdate(exchange, task);
        }

        if (method.equals("DELETE") ) {
            handleDelete(exchange, task);
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        send(exchange, 200, toJSON(tasks));
    }

    private void handleDetail(HttpExchange exchange, Task task) throws IOException {
        send(exchange, 200, toJSON(task));
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);//getBody를 만들어주어 밖에서 가져온다

        Task task = toTask(body);
        task.setId(generateId());
        tasks.add(task);

        send(exchange, 201, toJSON(task));
    }

    private void handleUpdate(HttpExchange exchange, Task task) throws IOException {
        String body = getBody(exchange);//getBody를 만들어주어 밖에서 가져온다
        Task source = toTask(body);

        task.setTitle(source.getTitle());

        send(exchange, 200, toJSON(task));
    }

    private void handleDelete(HttpExchange exchange, Task task) throws IOException {
        tasks.remove(task);

        send(exchange, 200, "");
    }


    private void send(HttpExchange exchange, int statusCode, String content)
            throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    //get을 사용시 그대로 나오며 값이 없다면 excption을 준다
    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
        //      .findFirst().get();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);

    }
    private String tasksTOJSON() throws IOException {
        return toJSON(tasks);
    }


    private String toJSON(Object object) throws IOException { //task로 넣어줫지만 object로 넣어줘도 문제없다
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object); //witeValue가 리플렉션을 이용해서 알아서 맞춰준다

        return outputStream.toString();
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

}
