package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.ToGet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final String OK_PATH = "tasks";

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println("method : " + method + " path : " + path);

        Map<String, Handle> methodMap = new HashMap<>();
        String[] pathSplit = path.split("/");

//        methodMap.put("GET", new ToGet());
//        methodMap.put("POST", new ToPost());
//        methodMap.put("PUT", new ToPut());
//        methodMap.put("DELETE", new ToDelete());
//        methodMap.get(method).check(pathSplit, body, exchange);

        switch (method) {
            case "GET"      : toGet(pathSplit, body, exchange);
                              break;
            case "POST"     : toPost(pathSplit, body, exchange);
                              break;
            case "PUT"      : toPut(pathSplit, body, exchange);
                              break;
            case "DELETE"   : toDelete(pathSplit, body, exchange);
                              break;
        }
    }

    private void toGet(String[] path, String body, HttpExchange exchange) throws IOException {
        if (path.length == 0) {
            responseOutput(OK, "Hello World", exchange);
            return;
        }

        if (!path[1].equals(OK_PATH)) {
            responseOutput(NOT_FOUND, "GET Path Error", exchange);
            return;
        }

        if (path.length >= 3) {
            int getNumber = Integer.parseInt(path[2]);
            if (!(tasks.size() <= getNumber -1)) {
                responseOutput(OK, tasks.get(getNumber -1).toString(), exchange);
                return;
            }
            responseOutput(NOT_FOUND, "해당 ID는 존재하지 않습니다.", exchange);
            return;
        }
        responseOutput(OK, tasksToJSON(), exchange);
    }

    private void toPost(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(OK_PATH)) {
            responseOutput(NOT_FOUND, "POST Path Error", exchange);
            return;
        }
        Task task = toTask(body);
        task.setId(id++);
        tasks.add(task);
        responseOutput(CREATED, "Create a new Task.", exchange);
    }

    private void toPut(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(OK_PATH) || body.isEmpty() || path.length < 3) {
            responseOutput(NOT_FOUND, "PUT Path or body Error", exchange);
            return;
        }
        int getNumber = Integer.parseInt(path[2]);

        if (!(tasks.size() <= getNumber -1)) {
            System.out.println("tasks Size : " + tasks.size() + "  search : " + (getNumber -1));
            Task task = toTask(body);
            tasks.get(getNumber -1).setTitle(task.getTitle());
            responseOutput(OK, tasks.get(getNumber -1).toString(), exchange);
            return;
        }
        responseOutput(OK, "id error", exchange);
    }

    private void toDelete(String[] path, String body, HttpExchange exchange) throws IOException {
        if (!path[1].equals(OK_PATH) || !body.isEmpty() || path.length < 3) {
            responseOutput(NOT_FOUND, "PUT Path or id Error", exchange);
            return;
        }
        Long getNumber1 = Long.parseLong(path[2]);

        for(Task task : tasks) {
            if(task.getId() == getNumber1){
                tasks.remove(task);
                responseOutput(OK, "delete success", exchange);
                return;
            }
        }
        responseOutput(NOT_FOUND, "해당하는 ID가 없습니다.", exchange);
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
