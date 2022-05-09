package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private TaskRepository taskRepository = new TaskRepository();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String TASK_DEFAULT_PATH = "/tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String body = getResponseBody(exchange);

        System.out.println(method + " " + path);

        try {
            // GET /tasks
            if (method.equals("GET") && path.equals(TASK_DEFAULT_PATH)) {
                List<Task> tasks = taskRepository.findAll();
                sendResponse(exchange, toJson(tasks), HttpStatus.OK);
                return;
            }

            // POST /tasks
            if (method.equals("POST") && path.equals(TASK_DEFAULT_PATH)) {
                Task task = toTask(body);
                Task savedTask = taskRepository.save(task);
                sendResponse(exchange, toJson(savedTask), HttpStatus.CREATED);
                return;
            }

            //GET /tasks/{id}
            if (method.equals("GET") && containPathVariable(path)) {
                Long id = getPathVariable(path);
                Task task = findTaskById(id);
                sendResponse(exchange, toJson(task), HttpStatus.OK);
                return;
            }

            // PUT /tasks/{id}
            if (method.equals("PUT") && containPathVariable(path)) {
                Long id = getPathVariable(path);
                Task target = findTaskById(id);
                Task source = toTask(body);
                target.setTitle(source.getTitle());
                sendResponse(exchange, toJson(task), HttpStatus.OK);
                return;
            }

            // DELETE /tasks/{id}
            if (method.equals("DELETE") && containPathVariable(path)) {
                Long id = getPathVariable(path);
                Task task = findTaskById(id);
                taskRepository.delete(task.getId());
                sendResponse(exchange, "", HttpStatus.NO_CONTENT);
                return;
            }

            sendResponse(exchange, "지원하지 않는 경로입니다", HttpStatus.NOT_FOUND);
        } catch (TaskNotFoundException exception) {
            sendResponse(exchange, exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id + "인 task는 존재하지 않습니다."));
    }

    private void sendResponse(HttpExchange exchange, String content, HttpStatus status) throws IOException {
        exchange.sendResponseHeaders(status.getValue(), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /***
     * 기본 경로를 받아서 ex) /tasks
     * PathVariable이 포함되어있는지 확인하는 메소드입니다. ex) /tasks/34
     * PathVariable은 정수여야 합니다.
     * @param defaultPath
     * @return boolean
     */
    private boolean containPathVariable(String defaultPath) {
        String pattern = TASK_DEFAULT_PATH + "/\\d+";
        return Pattern.matches(pattern, defaultPath);
    }

    private Long getPathVariable(String path) {
        String substring = path.substring(TASK_DEFAULT_PATH.length());// ex) /tasks/3 -> /3
        String pathVariable = substring.substring(1);
        return Long.parseLong(pathVariable);
    }

    private String getResponseBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return body;
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJson(Object from) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, from);
        return outputStream.toString();
    }
}
