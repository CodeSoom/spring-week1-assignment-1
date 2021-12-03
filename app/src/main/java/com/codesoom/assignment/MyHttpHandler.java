package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    TaskManager taskManager = new TaskManager();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));

        printRequestInfo(method, path, body);

        if (method.equals("GET") && path.equals("/tasks")) {
            readTasks(exchange);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            createTask(exchange, body);
        }

        if (method.equals("GET") && path.startsWith("/tasks/")) {
            readTask(exchange, path);
        }

        if (method.equals("DELETE") && path.startsWith("/tasks/")){
            deleteTask(exchange, path);
        }

        if ((method.equals("PUT") || method.equals("PATCH")) && path.startsWith("/tasks/")){
            updateTask(exchange, path, body);
        }

        String content = "";
        sendResponse(exchange, HttpStatusCode.NOT_FOUND, content);
    }

    private void updateTask(HttpExchange exchange, String path, String body) throws IOException {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        try {
            long id = Long.parseLong(idStr);
            Task task = toTask(body);
            taskManager.updateTask(id, task);
            String content = taskToJson(task);
            sendResponse(exchange, HttpStatusCode.OK, content);
        } catch (NoSuchElementException | NumberFormatException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.NOT_FOUND, "");
        } catch (JsonProcessingException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.BAD_REQUEST, "");
        }
    }

    private void deleteTask(HttpExchange exchange, String path) throws IOException {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        try {
            long id = Long.parseLong(idStr);
            taskManager.deleteTask(id);
            sendResponse(exchange, HttpStatusCode.NO_CONTENT, "");
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.BAD_REQUEST, "");
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.NOT_FOUND, "");
        }
    }


    private void createTask(HttpExchange exchange, String body) throws IOException {
        String content = "";
        try {
            Task task = toTask(body);
            taskManager.insertTask(task);
            content = taskToJson(task);
            sendResponse(exchange, HttpStatusCode.CREATED, content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.BAD_REQUEST, content);
        }
    }

    private void readTask(HttpExchange exchange, String path) throws IOException {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        String content = "";

        try {
            long id = Long.parseLong(idStr);
            Task task = taskManager.readTask(id);
            content = taskToJson(task);
            sendResponse(exchange, HttpStatusCode.OK, content);
        }
        catch (NoSuchElementException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.NOT_FOUND, content);
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            sendResponse(exchange, HttpStatusCode.NOT_FOUND, "");
        }

    }

    private void readTasks(HttpExchange exchange) throws IOException {
        String content = tasksToJson();
        sendResponse(exchange, HttpStatusCode.OK, content);
    }

    private void printRequestInfo(String method, String path, String requestBody) {
        System.out.println(method + " - " + path);
        if (!requestBody.isBlank()) {
            System.out.println(requestBody);
        }
    }

    private void sendResponse(HttpExchange exchange, HttpStatusCode httpStatusCode, String content) throws IOException {
        exchange.sendResponseHeaders(httpStatusCode.getValue(), content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.close();
    }

    private Task toTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    private String tasksToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskManager.tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
