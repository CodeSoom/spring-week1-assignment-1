package com.codesoom.assignment.controller;

import com.codesoom.assignment.Response;
import com.codesoom.assignment.dto.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;
    private final ObjectMapper mapper;

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
        Response result = null;
        if (path.matches("^/(tasks)$")) {
            result = switch (method) {
                case "GET" -> this.getAllTask();
                case "POST" -> this.createTask(exchange);
                default -> null;
            };
        } else if (path.matches("^/(tasks)/([0-9])*$")) {
            result = switch (method) {
                case "GET" -> this.getOneTask(exchange);
                case "PUT", "PATCH" -> this.updateTask(exchange);
                case "DELETE" -> this.deleteTask(exchange);
                default -> null;
            };
        }
        return result;
    }

    private Response getOneTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        Task task = taskRepository.findById(id);
        if (task == null) {
            return new Response(404, this.toJSON(null));
        }
        return new Response(200, this.toJSON(task));
    }

    private Response getAllTask() throws IOException {
        List<Task> ret = new ArrayList<>();
        Map<Long, Task> tasks = taskRepository.findAll();
        for(Long key: tasks.keySet()) {
            ret.add(tasks.get(key));
        }
        return new Response(200, this.toJSON(ret));
    }

    private Response createTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String content = this.getContent(inputStream);
        Task task = this.toTask(content);
        taskRepository.addTask(task);
        return new Response(201, this.toJSON(task));
    }

    private Response updateTask(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        InputStream inputStream = exchange.getRequestBody();
        String content = this.getContent(inputStream);
        Map<String, String> data = this.mapper.readValue(content, Map.class);
        Task task = taskRepository.updateTask(id, data.get("title"));
        if (task == null) {
            return new Response(404, this.toJSON(null));
        }
        return new Response(200, toJSON(task));
    }

    private Response deleteTask(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        Long id = getId(path);
        Task task = taskRepository.deleteTask(id);
        if (task == null) {
            return new Response(404, "");
        }
        return new Response(204, "");
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

}
