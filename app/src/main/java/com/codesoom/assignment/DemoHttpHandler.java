package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class DemoHttpHandler implements HttpHandler {
    private final List<Map<String, Task>> tasks = new ArrayList<>();
    private final Map<String, Task> taskMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static Long sequence = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final String body = createBody(exchange);

        System.out.println(method + " " + path);

        Map<String, String> contentAndStatusCode = checkPath(path, method, body);
        String content = contentAndStatusCode.get("content");
        int httpStatusCode = Integer.parseInt(contentAndStatusCode.get("httpStatusCode"));

        exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);

        OutputStream outputstream = exchange.getResponseBody();
        outputstream.write(content.getBytes());
        outputstream.flush();
        outputstream.close();
    }

    private Map<String, String> checkPath(String path, String method, String body) throws IOException {
        String id = checkPathGetId(path);
        Map<String, String> map = new HashMap<>();
        map.put("content", "");
        map.put("httpStatusCode", HttpStatus.INTERNAL_SERVER_ERROR.getCode() + "");

        if ("/".equals(path)) {
            if (HttpMethod.GET.getMethod().equals(method)) {
                map.put("content", "Todo List");
                map.put("httpStatusCode", HttpStatus.OK.getCode() + "");
            }
        }

        if ("/tasks".equals(path)) {
            if (HttpMethod.GET.getMethod().equals(method)) {
                map.put("content", tasksToJSON());
                map.put("httpStatusCode", HttpStatus.OK.getCode() + "");
            }
            if (HttpMethod.POST.getMethod().equals(method)) {
                createTask(body);
                map.put("content", tasksToJSON());
                map.put("httpStatusCode", HttpStatus.CREATED.getCode() + "");
            }
        }

        if (("/tasks/" + id).equals(path)) {
            if (HttpMethod.GET.getMethod().equals(method)) {
                Optional<Task> task = findId(id);
                map.put("httpStatusCode", HttpStatus.NOT_FOUND.getCode() + "");
                if (!task.isEmpty()) {
                    map.put("content", oneTaskToJSON(task.get()));
                    map.put("httpStatusCode", HttpStatus.OK.getCode() + "");
                }
            }
            if (HttpMethod.PUT.getMethod().equals(method) || HttpMethod.PATCH.getMethod().equals(method)) {
                Optional<Task> task = findId(id);
                map.put("httpStatusCode", HttpStatus.NOT_FOUND.getCode() + "");
                if (!task.isEmpty()) {
                    Task updateTask = updateTitle(task.get(), body);
                    map.put("content", oneTaskToJSON(updateTask));
                    map.put("httpStatusCode", HttpStatus.OK.getCode() + "");
                }
            }
            if (HttpMethod.DELETE.getMethod().equals(method)) {
                if (("/tasks/" + id).equals(path)) {
                    Optional<Task> task = findId(id);
                    map.put("httpStatusCode", HttpStatus.NOT_FOUND.getCode() + "");
                    if (!task.isEmpty()) {
                        deleteTodo(id);
                        map.put("httpStatusCode", HttpStatus.NO_CONTENT.getCode() + "");
                    }
                }
            }
        }
        return map;
    }

    private String createBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return body;
    }

    private void createTask(String body) throws JsonProcessingException {
        Task task = jsonToTask(body);
        task.setId(++sequence);
        taskMap.put(task.getId() + "", task);
        tasks.add(taskMap);
    }

    private String checkPathGetId(String path) {
        if (path.indexOf("/tasks/") == 0) {
            return path.replace("/tasks/", "");
        }
        return "";
    }

    private void deleteTodo(String id) {
        tasks.remove(id);
    }

    private Task updateTitle(Task task, String content) throws JsonProcessingException {
        Task originTask = jsonToTask(content);
        task.setTitle(originTask.getTitle());
        return task;
    }

    private String oneTaskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Optional findId(String id) {
        Optional<Task> task = Optional.empty();
        Task findTask = taskMap.get(id);
        if (findTask == null) {
            return task;
        }
        return task.of(findTask);
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
