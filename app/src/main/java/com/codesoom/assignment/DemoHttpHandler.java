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

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String num = path.replaceAll("[^0-9]", "");
        String content = "Hello world!";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(method.equals("GET") && path.contains("/tasks")){
            content = tasksToJSON(num);
            if(!body.isBlank()) {
                content = "GET은 조회만 가능합니다.";
            }
        }

        if(method.equals("POST") && path.contains("/tasks")){
            if (!body.isBlank()) {
                Task task = toTask(body);
                task.setId((long) tasks.size()+1);
                tasks.add(task);
            }
            content = tasksToJSON(num);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON(String num) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        if(!num.isEmpty()) {
            for (Task t : tasks) {
                if (t.getId() == Long.parseLong(num)) {
                    objectMapper.writeValue(outputStream, t);
                    return outputStream.toString();
                }
            }
        }
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }




}
