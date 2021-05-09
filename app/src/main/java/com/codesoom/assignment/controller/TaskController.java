package com.codesoom.assignment.controller;

import com.codesoom.assignment.HttpStatus;
import com.codesoom.assignment.Response;
import com.codesoom.assignment.dto.Task;
import com.codesoom.assignment.exception.DoesNotExistException;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;
    private final ObjectMapper mapper;
    private Long newId = -1L;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean handleResource(String path) {
        return path.matches("^/(tasks)(/[0-9])*$");
    }

    @Override
    public Response resolve(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        return this.route(exchange, path, method);
    }

    private Response getOneTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        try {
            Task task = taskRepository.findById(id);
            return new Response(HttpStatus.OK, this.toJSON(task));
        } catch (DoesNotExistException e) {
            return new Response(HttpStatus.NOT_FOUND);
        }
    }

    private Response getAllTask() throws IOException {
        List<Task> tasks = taskRepository.findAll();
        return new Response(HttpStatus.OK, this.toJSON(tasks));
    }

    private Response createTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String content = this.getContent(inputStream);
        Task task = this.toTask(content);
        task.setId(generateId());
        taskRepository.addTask(task);
        return new Response(HttpStatus.CREATED, this.toJSON(task));
    }

    private Response updateTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        InputStream inputStream = exchange.getRequestBody();
        String content = this.getContent(inputStream);
        Map<String, String> data = this.mapper.readValue(content, Map.class);
        try {
            Task task = taskRepository.updateTask(id, data.get("title"));
            return new Response(HttpStatus.OK, toJSON(task));
        } catch (DoesNotExistException e) {
            return new Response(HttpStatus.NOT_FOUND);
        }
    }

    private Response deleteTask(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        if (taskRepository.deleteTask(id)) {
            return new Response(HttpStatus.NO_CONTENT);
        } else {
            return new Response(HttpStatus.NOT_FOUND);
        }
    }

    private Long getId(String path) {
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        return Long.parseLong(idStr);
    }

    private String getContent(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private String toJSON(Object obj) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.mapper.writeValue(outputStream, obj);
        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return this.mapper.readValue(content, Task.class);
    }

    private Response route(HttpExchange exchange, String path, String method) throws IOException {
        Response result;
        if (path.matches("^/(tasks)$")) {
            return switch (method) {
                case "GET" -> this.getAllTask();
                case "POST" -> this.createTask(exchange);
                default -> new Response(HttpStatus.NOT_FOUND);
            };
        } else if (path.matches("^/(tasks)/([0-9])*$")) {
            return switch (method) {
                case "GET" -> this.getOneTask(exchange);
                case "PUT", "PATCH" -> this.updateTask(exchange);
                case "DELETE" -> this.deleteTask(exchange);
                default -> new Response(HttpStatus.NOT_FOUND);
            };
        }
        result = new Response(HttpStatus.NOT_FOUND);
        return result;
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }
}
