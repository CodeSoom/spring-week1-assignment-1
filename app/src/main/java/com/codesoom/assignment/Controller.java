package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    List<Task> tasks = new ArrayList<>();
    Long id = 1L;

    private ObjectMapper objectMapper = new ObjectMapper();
    private OutputStream outputStream;

    public Task getTask(Long idValue) {
        return tasks.stream()
                .filter(task -> task.getId().equals(idValue))
                .findFirst().orElse(null);
    }

    public void requestHttp(String method, String path, String body, HttpExchange exchange) throws IOException {

        Long idValue = Long.parseLong(path.substring("/tasks/".length()));

        if (method.equals("GET")) {
            if (!path.startsWith("/tasks")) {
                send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            if (path.equals("/tasks")) {
                send(exchange, taskToJson(tasks), HttpStatus.OK.getHttpStatus());
                return;
            }

            handleGet(idValue, exchange, HttpStatus.OK.getHttpStatus());
        } else if (method.equals("POST")) {
            if (!path.startsWith("/tasks")) {
                send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            handleCreate(body, exchange, HttpStatus.OK.getHttpStatus());
        } else if (method.equals("PUT") || method.equals("PATCH")) {
            if (!path.startsWith("/tasks")) {
                send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            handleUpdate(idValue, body, exchange, HttpStatus.OK.getHttpStatus());
        } else if (method.equals("DELETE")) {
            if (!path.startsWith("/tasks")) {
                send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
                return;
            }

            handleDelete(idValue, exchange, HttpStatus.NO_CONTENT.getHttpStatus());
        }
    }

    private void handleGet(Long idValue, HttpExchange exchange, int HttpStatusCode) throws IOException {
        Task getTask = getTask(idValue);
        if (getTask == null) {
            send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
            return;
        }

        send(exchange, taskToJson(getTask), HttpStatusCode);
    }

    private void handleCreate(String body, HttpExchange exchange, int HttpStatusCode) throws IOException {
        String requestTitle = body.split("\"")[1];
        if (!requestTitle.equals("title")) {
            send(exchange, "", HttpStatus.BAD_REQUEST.getHttpStatus());
            return;
        }

        Task createTask = jsonToTask(body);
        createTask.setId(id++);
        tasks.add(createTask);
        send(exchange, taskToJson(createTask), HttpStatusCode);
    }

    private void handleUpdate(Long idValue, String body, HttpExchange exchange, int HttpStatusCode) throws IOException {
            Task updateTask = getTask(idValue);
            if (updateTask == null) {
                send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
                return;
            }

            Task task = jsonToTask(body);
            updateTask.setTitle(task.getTitle());
            send(exchange, taskToJson(updateTask), HttpStatusCode);
    }

    private void handleDelete(Long idValue, HttpExchange exchange, int StatusCode) throws IOException {
        Task deleteTask = getTask(idValue);

        if (deleteTask == null) {
            send(exchange, "", HttpStatus.NOT_FOUND.getHttpStatus());
            return;
        }

        tasks.remove(deleteTask);
        send(exchange, "", StatusCode);
    }

    public void send(HttpExchange exchange, String content, int HttpStatus) throws IOException {
        exchange.sendResponseHeaders(HttpStatus, content.getBytes().length);
        outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public Task jsonToTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    public String taskToJson(Object object) throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

}
