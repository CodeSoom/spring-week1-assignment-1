package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpringHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    static final int httpStatus = 200;

    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();



        System.out.println(method + " " + path);

        if(path.startsWith("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        send(exchange, httpStatus, "매일 매일 달리지기 위한 첫걸음 시작하기!");
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            send(exchange, httpStatus, toJSON(tasks));
        }

        if(method.equals("POST")) {
//
            String body = getBody(exchange);
            if( !body.isBlank()) {

                Task task = toTask(body);
                task.setId(generateId());
                tasks.add(task);

                send(exchange, httpStatus, toJSON(task));
            }

        }
    }



    private Long generateId() {
        newId = 1L;
        return newId;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }


    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }


    private Task toTask(String content) throws JsonProcessingException {
       return objectMapper.readValue(content, Task.class);

    }

    private String tasksToJSON() throws IOException {
        return toJSON(tasks);

    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}