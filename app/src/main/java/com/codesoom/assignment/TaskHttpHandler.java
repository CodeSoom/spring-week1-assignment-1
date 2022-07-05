package com.codesoom.assignment;

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

// Task에 대한 응답과 요청을 처리하는 핸들러
public class TaskHttpHandler implements HttpHandler {
    public static final String NOT_FOUND = "404 Not Found";
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long id = 0L;

    public TaskHttpHandler() {
        Task task = new Task();
        task.setId(id++);
        task.setTitle("a");
        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String content = "200 OK";
        content = validPath(path, content);

        InputStream requestBody = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Created";
            if (!body.isBlank()) {
                Task task = toTask(body);
                tasks.add(task);
            }
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }
    // 요청온 경로가 유효하지 않으면 에러 메시지 반환
    private String validPath(String path, String content) {
        if (!path.equals("/tasks")) {
            content = NOT_FOUND;
        }
        return content;
    }

    // 요청 받은 content를 Task 객체로 매핑하고 리턴
    private Task toTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(id++);
        return task;
    }
    // 저장되어 있는 tasks을 Json으로 변환하는 메서드
    private String tasksToJson() throws IOException {
        return objectMapper.writeValueAsString(tasks);
    }
}
