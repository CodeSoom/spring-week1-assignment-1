package com.codesoom.assignment.httpHandlers;

import com.codesoom.assignment.httpHandlers.exceptions.TaskNotFoundException;
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

    final private List<Task> tasks = new ArrayList<>();
    private long taskId = 0;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();
        String path = requestURI.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "";
        int statusCode = 200;

        System.out.println(requestMethod + " " + requestURI + " " + requestBody);

        try {
            //TODO: 리팩토링
            if (requestMethod.equals("GET") && path.equals("/tasks")) {
                content = tasksToJSON();
                exchange.sendResponseHeaders(statusCode, content.getBytes().length);
            } else if (requestMethod.equals("GET") && path.startsWith("/tasks/")) {
                long id = Long.parseLong(path.substring(7));

                Task task = findTaskById(id);
                if (task == null) {
                    throw new TaskNotFoundException();
                }

                statusCode = 200;
                content = toJson(task);
                exchange.sendResponseHeaders(statusCode, content.getBytes().length);
            } else if (requestMethod.equals("POST") && path.equals("/tasks")) {
                if (requestBody.isBlank()) {
                    throw new IllegalArgumentException("requestBody가 비었습니다.");
                }

                Task task = toTask(requestBody);
                task.setId(taskId);
                taskId++;
                this.tasks.add(task);
                statusCode = 201;
                content = toJson(task);
                exchange.sendResponseHeaders(statusCode, content.getBytes().length);
            } else if (requestMethod.equals("PUT") && path.startsWith("/tasks")) {
                long id = Long.parseLong(path.substring(7));

                Task task = findTaskById(id);
                if (task == null) {
                    throw new TaskNotFoundException();
                } else {
                    Task updateTask = toTask(requestBody);
                    //TODO: id도 업데이트 필요??
                    task.setTitle(updateTask.getTitle());
                    content = toJson(task);
                    exchange.sendResponseHeaders(statusCode, content.getBytes().length);
                }
            } else if (requestMethod.equals("PATCH") && path.startsWith("/tasks")) {
                long id = Long.parseLong(path.substring(7));

                Task task = findTaskById(id);
                if (task == null) {
                    throw new TaskNotFoundException();
                } else {
                    Task updateTask = toTask(requestBody);
                    task.setTitle(updateTask.getTitle());
                    content = toJson(task);
                    exchange.sendResponseHeaders(statusCode, content.getBytes().length);
                }
            } else if (requestMethod.equals("DELETE") && path.startsWith("/tasks")) {
                long id = Long.parseLong(path.substring(7));
                boolean found = false;

                for (Task task : tasks) {
                    if (task.getId() == id) {
                        found = true;
                        System.out.println("id: " + id);
                        boolean remove = tasks.remove(task);
                        System.out.println("remove " + remove);
                        statusCode = 204;
                        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
                        break;
                    }
                }
                if (!found) {
                    throw new TaskNotFoundException();
                }
            }

        } catch (TaskNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 404;
            content = e.getMessage();
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 400;
            content = e.getMessage();
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 400;
            content = e.getMessage();
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        } finally {
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(content.getBytes());
            responseBody.flush();
            responseBody.close();
        }
    }

    private Task findTaskById(long id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }


    // TODO: objectMapper 메서드들 확인할 것.
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJson(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, this.tasks);

        return outputStream.toString();
    }
}
