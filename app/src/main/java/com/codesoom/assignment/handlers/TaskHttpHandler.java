package com.codesoom.assignment.handlers;

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

import static com.codesoom.assignment.App.*;

public class TaskHttpHandler implements HttpHandler {
    List<Task> tasks;
    ObjectMapper objectMapper = new ObjectMapper();

    public TaskHttpHandler() {
        tasks = new ArrayList<Task>();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String requestURIPath = requestURI.getPath();
        String requestMethod = exchange.getRequestMethod();
        String result = "";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));



        if(requestMethod.equals("GET")){
            result = getMapper(requestURIPath);
            exchange.sendResponseHeaders(STATUS_OK, result.getBytes().length);
        }else if(requestMethod.equals("POST")) {
            if(body.isBlank()){
                result = "Title must not empty!!";
                exchange.sendResponseHeaders(STATUS_BAD_REQUEST, result.getBytes().length);
            }else{
                result = setTask(body);
                exchange.sendResponseHeaders(STATUS_CREATED, result.getBytes().length);
            }
        }else if(requestMethod.equals("PUT")){
            String[] split = requestURIPath.split("/");
            if(body.isBlank()){
                result = "Title must not empty!!";
                exchange.sendResponseHeaders(STATUS_BAD_REQUEST, result.getBytes().length);
            }else if(split.length > 0 && split.length != 1){
                System.out.println(split[2]);
                long parseLong = Long.parseLong(split[2]);
                result = updateTask(body, parseLong);
                exchange.sendResponseHeaders(STATUS_OK, result.getBytes().length);
            }else{
                result = "Invalid ID!";
                exchange.sendResponseHeaders(STATUS_BAD_REQUEST, result.getBytes().length);
            }
        }else if(requestMethod.equals("DELETE")){
            String[] split = requestURIPath.split("/");
            if(split.length > 0 && split.length != 1){
                System.out.println(split[2]);
                long parseLong = Long.parseLong(split[2]);
                result = deleteTask(body, parseLong);
                exchange.sendResponseHeaders(STATUS_OK, result.getBytes().length);
            }else{
                result = "Invalid ID!";
                exchange.sendResponseHeaders(STATUS_BAD_REQUEST, result.getBytes().length);
            }
        }else{
            result = "405 Method Not Allowed!";
            exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, result.getBytes().length);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(result.getBytes());
        responseBody.flush();
        exchange.close();
    }

    /**
     * Method Get Mapper
     * */

    private String getMapper(String requestURIPath) throws IOException {
        String result = "";
        if(requestURIPath.equals("/tasks")){
            result = getTasks();
        }else if(requestURIPath.contains("/tasks")){
            String[] split = requestURIPath.split("/");
            if(split.length > 0 &&  split.length != 1){
                long parseLong = Long.parseLong(split[2]);
                result = getTask(parseLong);
            }else{
                result = "Wrong argument passed!";
            }
        }
        return result;
    }

    /**
     * Get Stored Tasks
     * */

    private String getTasks() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        System.out.println(outputStream.toString());
        return outputStream.toString();
    }

    /**
     * Get Stored Task
     * */

    private String getTask(long ID) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Task task = tasks.get((int) ID - 1);
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    /**
     * Create New Task
     * */

    private String setTask(String jsonString) throws IOException {
        Task taskJson = toTask(jsonString);
        if(tasks.size() > 0){
            taskJson.setId(tasks.get(tasks.size() - 1).getId() + 1L);
        }
        tasks.add(taskJson);
        return getTasks();
    }

    /**
     * Update The Task
     * */

    private String updateTask(String jsonString, long ID) throws IOException {
        Task taskJson = toTask(jsonString);
        if(tasks.size() > 0){
            tasks.get((int) ID - 1 ).setTitle(taskJson.getTitle());
        }
        return getTasks();
    }

    /**
     * Delete The Task
     * */

    private String deleteTask(String jsonString, long ID) throws IOException {
        Task taskJson = toTask(jsonString);
        if(tasks.size() > 0){
            tasks.remove((int) ID - 1 );
        }
        return getTasks();
    }

    /**
     * Converting Json Object to Task
     * */

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

}
