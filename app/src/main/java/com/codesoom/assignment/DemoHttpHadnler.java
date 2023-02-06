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

public class DemoHttpHadnler implements HttpHandler {
    private Long testId;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private static final String TASK_DETAIL_PATH = "/tasks/";
    private static final String PATH_SPLIT_SYMBOL = "/";


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        InputStream inputStream = exchange.getRequestBody();
        String content = "";

        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println("method::" + requestMethod + "\npath:: " + path);


        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if (requestMethod.equals("GET") && path.equals(TASK_DETAIL_PATH)) {
            try {
                Task task = findTask(taskId(path));
                task.setTitle(body);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            if (!body.isBlank()) {
                Task task = toTask(body);
                tasks.add(task);
            }
            content = "Create a new task";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long taskId(String path) {
        return Long.parseLong(path.split(PATH_SPLIT_SYMBOL)[2]);
    }

    private Task findTask(Long taskId) throws Exception {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(Exception::new);
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
