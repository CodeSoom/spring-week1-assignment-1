package com.codesoom.assignment;

import com.codesoom.assignment.task.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServerHttpHandler implements HttpHandler {

    // Mapper, tasks 생성
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

    private int idCount = 1;

    // Handler 설정??
    public ServerHttpHandler() {
        Task task = new Task();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        // InputStream 설정
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));


        // Response 내용 초기화
        int response = 400;

        // 출력 내용 초기화
        String content = "";

        // GET method 설정
        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            response = 200;
        }

        // POST method 설정
        if (method.equals("POST") && path.equals("/tasks") && !body.isBlank()) {
            Task task = toTask(body);
            task.setId(idCount);
            idCount++;

            tasks.add(task);

            content = tasksToJSON();
            response = 201;
        }

        // 통신 결과 보고
        exchange.sendResponseHeaders(response, content.getBytes().length);

        // OutputStream 내용 지정, 버퍼 지우기, 닫기
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        if (!outputStream.toString().isBlank()) {
            return outputStream.toString();
        }

        return "[]";

    }

}
