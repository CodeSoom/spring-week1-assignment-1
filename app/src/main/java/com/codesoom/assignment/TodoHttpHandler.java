package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private int index;
    private int statusCode = 200;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String content = "";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (method.equals("GET")) {
            if (hasNumberParameter(path)) {
                if (hasIndex(index - 1)) {
                    statusCode = 200;
                    content = taskToJson(index - 1);
                } else
                    statusCode = 404;
            } else {
                statusCode = 200;
                content = taskToJson();
            }
        } else if (method.equals("POST") && path.equals("/tasks")) {
            Task task = jsonToTask(body);
            tasks.add(task);
            statusCode = 201;
            content = taskToJson(task.getId() - 1);
        } else if (method.equals("PUT") || method.equals("PATCH")) {
            if (hasNumberParameter(path)) {
                if (hasIndex(index - 1)) {
                    tasks.remove(index - 1);
                    Task task = jsonToTask(body);
                    task.setId((long) index);
                    tasks.add(index - 1, task);
                    statusCode = 200;
                    content = taskToJson(index - 1);
                } else
                    statusCode = 404;

            } else {
                statusCode = 404;
            }
        } else if (method.equals("DELETE")) {
            if (hasNumberParameter(path)) {
                if (hasIndex(index - 1)) {
                    statusCode = 204;
                    tasks.remove(index - 1);
                } else
                    statusCode = 404;
            } else {
                statusCode = 404;
            }
        }
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private boolean hasIndex(int index) {
        if (index < 0 || tasks.size() <= index) {
            return false;
        }
        return true;
    }

    private boolean hasNumberParameter(String path) {
        if (path.startsWith("/tasks/")) {
            String[] split = path.split("/");
            if (split.length == 3) {
                index = Integer.parseInt(split[2]);
                return true;
            }
        }
        return false;
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId((long) (tasks.size() + 1));
        return task;
    }

    private String taskToJson() throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks);
        return outputstream.toString();
    }

    private String taskToJson(long index) throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks.get((int) index));
        return outputstream.toString();
    }
}
