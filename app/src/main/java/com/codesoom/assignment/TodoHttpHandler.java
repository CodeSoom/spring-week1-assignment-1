package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.Title;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";
    private static final String BASIC_URI = "/tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String content = "";
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // TODO: 나중에 mehotd들 enum으로 변경해보기
        if (method.equals(GET)) {
            if (path.equals(BASIC_URI)) {
                content = tasksToJSON();
            } else {
                Long id = getId(path);
                Task task = taskMap.get(id);
                content = taskToJSON(task);
            }
        } else if (method.equals(POST)) {
            if(!body.isBlank()) {
                Task task = toTask(body);
                taskMap.put(task.getId(), task);
                Long lastSequence = Task.getSequence();
                Task lastTask = taskMap.get(lastSequence);
                content = taskToJSON(lastTask);
            } else {
                content = "";
            }
        } else if (method.equals(PUT)) {
            Long id = getId(path);
            Task task = taskMap.get(id);

            Task changeTask = toTask(body);
            task.setTitle(changeTask.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);

        } else if (method.equals(PATCH)) {
            Long id = getId(path);
            Task task = taskMap.get(id);

            Title title = toTitle(body);  //body를 직접 쓸시 인코딩 깨짐 문제 존재. 인코딩 방식 찾지 못해 Title 객체 만들어서 body를 전환하는 걸로.
            task.setTitle(title.getTitle());
            taskMap.put(id, task);

            content = taskToJSON(task);
        } else if (method.equals(DELETE)) {
            Long id = getId(path);
            taskMap.remove(id);
            content = "";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Title toTitle(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Title.class);
    }

    private Long getId(String path) {
        String[] splits = path.split("/");
        return Long.parseLong(splits[splits.length - 1]);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskMap.values());

        return outputStream.toString();
    }
}
