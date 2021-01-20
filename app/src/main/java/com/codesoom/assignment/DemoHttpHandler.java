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
    static final int BAD_REQUEST = 400;
    static final int NOT_FOUND = 404;
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

        String content = "Hello World";

        if(method.equals("GET")) {
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(BAD_REQUEST,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            if(path.equals("/tasks")) {
                content = (tasks == null ) ? "[]" : tasksToJson();
                exchange.sendResponseHeaders(OK,content.getBytes().length);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task getTask = findTask(idValue);

            if(getTask == null) {
                exchange.sendResponseHeaders(NOT_FOUND,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            content = taskToJson(getTask);
            exchange.sendResponseHeaders(OK,content.getBytes().length);
            writeContentWithOutputStream(exchange, content);
        }

        else if(method.equals("POST")) {
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(BAD_REQUEST,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            String requestTitle = body.split("\"")[1];

            if(!requestTitle.equals("title")) {
                exchange.sendResponseHeaders(BAD_REQUEST,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            Task task = toTask(body);
            createTask(task);
            content = taskToJson(task);
            exchange.sendResponseHeaders(CREATED,content.getBytes().length);
            writeContentWithOutputStream(exchange, content);
        }

        else if((method.equals("PUT") || method.equals("PATCH"))){
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(BAD_REQUEST,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task updateTask = findTask(idValue);

            if(updateTask == null) {
                exchange.sendResponseHeaders(NOT_FOUND, 0);
                writeContentWithOutputStream(exchange, content);
                return;
            }

            Task task = toTask(body);
            updateTask.setTitle(task.getTitle());
            content = taskToJson(updateTask);
            exchange.sendResponseHeaders(OK,content.getBytes().length);
            writeContentWithOutputStream(exchange, content);
        }

        else if(method.equals("DELETE")) {
            if(!path.startsWith("/tasks")) {
                exchange.sendResponseHeaders(BAD_REQUEST,0);
                writeContentWithOutputStream(exchange, "");
                return;
            }

            Long idValue = Long.parseLong(path.substring(7));
            Task deleteTask = findTask(idValue);

            if(deleteTask==null) {
                exchange.sendResponseHeaders(NOT_FOUND, 0);
                writeContentWithOutputStream(exchange, content);
                return;
            }

            tasks.remove(deleteTask);
            content = "";
            exchange.sendResponseHeaders(OK,0);
            writeContentWithOutputStream(exchange, content);
        }
    }

    private void createTask(Task task) {
        task.setId(id++);
        tasks.add(task);
    }

    private void writeContentWithOutputStream(HttpExchange exchange, String content) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task findTask(Long idValue) {
        Task getTask = null;
        for(Task task : tasks){
            if(task.getId() == idValue){
                getTask = task;
                break;
            }
        }
        return getTask;
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
