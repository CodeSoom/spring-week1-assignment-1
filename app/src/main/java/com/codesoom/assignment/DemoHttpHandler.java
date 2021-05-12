package com.codesoom.assignment;

import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpStatus;
import com.codesoom.assignment.enums.TodoURI;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 1L;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream)))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(
                String.format("%s %s Something requests...",
                        method,
                        path
                )
        );

        int responseCode = HttpStatus.OK.code();
        String responseContent = "";

        try {
            if (method.equals(HttpMethod.GET.name()) && path.matches(TodoURI.TASKS.uri())) {
                responseContent = tasksToJson();
            }

            if (method.equals(HttpMethod.GET.name()) && path.matches(TodoURI.TASKS_ID.uri())) {
                Long fetchId = Long.parseLong(getTaskId(path));

                if (fetchId == 0 || !isExistTask(fetchId)) {
                    responseCode = HttpStatus.NOT_FOUND.code();
                    throw new IllegalArgumentException("404 Not Found error");
                }

                Task task = fetchOneTask(fetchId);
                System.out.println(task.toString());

                responseContent = taskToJson(task);
            }

            if (method.equals(HttpMethod.POST.name()) && path.matches(TodoURI.TASKS.uri())) {
                if (body.isBlank()) {
                    responseCode = HttpStatus.BAD_REQUEST.code();
                    throw new IllegalArgumentException("400 Bad request");
                }

                Task task = JsonToTask(body);
                task.setId(id++);
                tasks.add(task);

                responseCode = HttpStatus.CREATED.code();
                responseContent = taskToJson(task);
            }

            if (method.equals(HttpMethod.PUT.name()) && path.matches(TodoURI.TASKS_ID.uri())) {
                Long fetchId = Long.parseLong(getTaskId(path));

                if (fetchId == 0 || !isExistTask(fetchId)) {
                    responseCode = HttpStatus.NOT_FOUND.code();
                    throw new IllegalArgumentException("404 Not Found error");
                }

                String fetchTitle = JsonToTask(body).getTitle();
                Task task = updateOneTask(fetchId, fetchTitle);

                responseContent = taskToJson(task);
            }

            if (method.equals(HttpMethod.DELETE.name()) && path.matches(TodoURI.TASKS_ID.uri())) {
                Long deleteId = Long.parseLong(getTaskId(path));

                if (deleteId == 0 || !isExistTask(deleteId)) {
                    responseCode = HttpStatus.NOT_FOUND.code();
                    throw new IllegalArgumentException("404 Not Found error");
                }

                deleteOneTask(deleteId);

                responseCode = HttpStatus.NO_CONTENT.code();
            }

        } catch (IllegalArgumentException iae) {
            iae.getMessage();
        } finally {
            byte[] responseBytes = responseContent.getBytes();
            int responseLength = (responseCode != HttpStatus.NO_CONTENT.code()) ? responseBytes.length : -1;

            exchange.sendResponseHeaders(responseCode, responseLength);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(responseBytes);
            outputStream.flush();
            outputStream.close();
        }
    }

    private Task JsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        if (tasks.size() == 0) {
            return "[]";
        }

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        if (task == null) {
            return "{}";
        }

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private boolean isExistTask(Long id) {
        return tasks.stream().anyMatch(t -> t.getId() == id);
    }

    private String getTaskId(String path) {
        return path.replaceAll("[^0-9]", "");
    }

    private Task fetchOneTask(Long id) {
        return tasks.stream()
                .filter((t) -> t.getId() == id)
                .findFirst()
                .get();
    }

    private Task updateOneTask(Long fetchId, String fetchTitle) {
        Task findTask = tasks.stream()
                .filter((t) -> t.getId() == fetchId)
                .findFirst()
                .get();
        findTask.setTitle(fetchTitle);
        return findTask;
    }

    private boolean deleteOneTask(Long id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
}
