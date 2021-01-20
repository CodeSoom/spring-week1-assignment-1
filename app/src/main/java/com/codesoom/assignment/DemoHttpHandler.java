package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 1L;
    private int failStatusCode = 404;
    private int successStatusCode = 200;
    private String checkPath = "tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println("method : " + method + "         path : " + path);

        String[] pathSplit = path.split("/");

        if (method.equals("GET")) {
            toGet(pathSplit, body, exchange);
            return;
        }

        if (method.equals("POST")) {
            toPost(pathSplit, body, exchange);
            return;
        }

        if (method.equals("PUT")){
            toPut(pathSplit, body, exchange);
            return;
        }

        if (method.equals("DELETE")){
            toDelete(pathSplit, body, exchange);
            return;
        }
    }

    private void toGet(String[] path, String body, HttpExchange exchange) throws IOException {
        if (path.length == 0) {
            responseOutput(successStatusCode, "Hello World", exchange);
            return;
        }

        if (!path[1].equals(checkPath)) {
            responseOutput(failStatusCode, "GET Path Error", exchange);
            return;
        }

        if (path.length >= 3) {
            int getNumber = Integer.parseInt(path[2]);
            if (!(tasks.size() <= getNumber -1)) {
                responseOutput(successStatusCode, tasks.get(getNumber -1).toString(), exchange);
                return;
            }
            responseOutput(failStatusCode, "해당 ID는 존재하지 않습니다.", exchange);
            return;
        }
        responseOutput(successStatusCode, tasksToJSON(), exchange);
    }

    private void toPost(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(checkPath)) {
            responseOutput(failStatusCode, "POST Path Error", exchange);
            return;
        }
        Task task = toTask(body);
        task.setId(id++);
        tasks.add(task);
        responseOutput(201, "Create a new Task.", exchange);
    }

    private void toPut(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(checkPath) || body.isEmpty() || path.length < 3) {
            responseOutput(failStatusCode, "PUT Path or body Error", exchange);
            return;
        }
        int getNumber = Integer.parseInt(path[2]);

        if (!(tasks.size() <= getNumber -1)) {
            System.out.println("tasks Size : " + tasks.size() + "  search : " + (getNumber -1));
            Task task = toTask(body);
            tasks.get(getNumber -1).setTitle(task.getTitle());
            responseOutput(successStatusCode, tasks.get(getNumber -1).toString(), exchange);
            return;
        }
        responseOutput(successStatusCode, "id error", exchange);
    }

    private void toDelete(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(checkPath) || !body.isEmpty() || path.length < 3) {
            responseOutput(failStatusCode, "PUT Path or id Error", exchange);
            return;
        }
        Long getNumber1 = Long.parseLong(path[2]);

        for(Task task : tasks) {
            if(task.getId() == getNumber1){
                tasks.remove(task);
                responseOutput(successStatusCode, "delete success", exchange);
                return;
            }
        }
        responseOutput(failStatusCode, "해당하는 ID가 없습니다.", exchange);
    }

    private void responseOutput(int statusCode, String content, HttpExchange exchange) throws IOException {
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
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}