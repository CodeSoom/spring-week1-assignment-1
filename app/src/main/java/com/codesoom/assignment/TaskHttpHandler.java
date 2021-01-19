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

public class TaskHttpHandler implements HttpHandler {
    ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String content = "OK";

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println(method + " " + path);

        // "/tasks/1" is split into { "", "tasks", "1" }
        String[] segments = path.split("/");

        if (segments.length < 2 || segments.length > 3 || !segments[0].isBlank() || !segments[1].equals("tasks")) {
            // 404 NotFound Exception
            System.out.println("[Invalid URI] 404 NotFound Exception");
            exchange.sendResponseHeaders(404, 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.flush();
            outputStream.close();
            return;
        }

        int id = 0;
        if (segments.length == 3) {
            // if segments[2] is not integer, 400 BadRequest - NumberFormatException thrown
            id = Integer.parseInt(segments[2]);
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (method.equals("GET") ) {
            if (segments.length == 2) {
                content = taskToJSON();
            }

            if (segments.length == 3) {
                boolean isthere = false;
                for (Task task : tasks) {
                    if(task.getId() == id) {
                        isthere = true;
                        content = taskToJSON(task);
                        break;
                    }
                }

                if (isthere == false) {
                    // 404 NotFound Exception
                    System.out.println("[GET] 404 NotFound Exception");
                    exchange.sendResponseHeaders(404, 0);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.flush();
                    outputStream.close();
                    return;
                }
            }
        }

        if (method.equals("POST")) {
            if (!body.isBlank() && (segments.length == 2)) {
                Task task = toTask(body);

                if (tasks.isEmpty()) {
                    if(task.getId() == null) task.setId(1L);
                }
                else {
                    int size = tasks.size();
                    Long lastId = tasks.get(size - 1).getId();

                    if (task.getId() == null) {
                        task.setId(lastId + 1);
                    }
                    else {
                        if (task.getId() <= lastId) {
                            // 400 BadRequest Exception
                            System.out.println("[POST] 400 BadRequest Exception");
                            exchange.sendResponseHeaders(400, 0);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.flush();
                            outputStream.close();
                            return;
                        }
                    }
                }

                tasks.add(task);
                content = "Task successfully added.";
            }
            else {
                // 400 BadRequest Exception
                System.out.println("[POST] 400 BadRequest Exception");
                exchange.sendResponseHeaders(400, 0);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.flush();
                outputStream.close();
                return;
            }
        }

        if (method.equals("PUT")) {

        }

        if (method.equals("PATCH")) {

        }

        if (method.equals("DELETE")) {

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

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String taskToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
