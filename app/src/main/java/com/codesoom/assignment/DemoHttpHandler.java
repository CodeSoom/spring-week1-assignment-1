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
    static final int OK = 200;
    static final int CREATED = 201;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id=1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method+" "+path);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(!body.isBlank()) {
            System.out.println(body);
//            Task task = toTask(body);
//            task.setId(id++);
//            tasks.add(task);
        }

        String content = "Hello World";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = (tasks == null ) ? "[]" : tasksToJson();
            exchange.sendResponseHeaders(OK,content.getBytes().length);
        }

        else if(method.equals("GET") && path.startsWith("/tasks")) {
            Task findTask = null;
            Long idValue = Long.parseLong(path.substring(7));
            System.out.println(idValue);
            for(Task task : tasks){
                if(task.getId() == idValue){
                    findTask = task;
                    break;
                }
            }

            content = taskToJson(findTask);
            exchange.sendResponseHeaders(OK,content.getBytes().length);
        }

        else if(method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(body);
            task.setId(id++);
            tasks.add(task);

            content = taskToJson(task);
            exchange.sendResponseHeaders(CREATED,content.getBytes().length);
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }
}
