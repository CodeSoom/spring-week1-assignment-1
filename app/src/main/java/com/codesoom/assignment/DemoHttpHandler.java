package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private TaskRepository taskRepository = new TaskRepository();
    ObjectMapper objectMapper = new ObjectMapper();

    private final String TASK_DEFAULT_PATH = "/tasks";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String body = getResponseBody(exchange);

        System.out.println(method + " " + path);

        // GET /task
        if (method.equals("GET") && path.equals(TASK_DEFAULT_PATH)) {
            List<Task> tasks = taskRepository.findAll();
            sendResponse(exchange, toJson(tasks), 200);
            return;
        }

        // POST /tasks
        if (method.equals("POST") && path.equals(TASK_DEFAULT_PATH)) {
            Task task = toTask(body);
            Long savedId = taskRepository.save(task);

            Task savedTask = taskRepository.findById(savedId);
            sendResponse(exchange, toJson(savedTask), 201);
            return;
        }

        //GET /tasks/{id}
        if (method.equals("GET") && containPathVariable(path)) {
            Long id = getPathVariable(path);

            Task task = taskRepository.findById(id);

            sendResponse(exchange, toJson(task), 200);
            return;
        }

        // PUT /tasks/{id}
        if (method.equals("PUT") && containPathVariable(path)) {
            Long id = getPathVariable(path);
            Task task = toTask(body);

            taskRepository.update(id, task);

            Task updatedTask = taskRepository.findById(id);

            sendResponse(exchange, toJson(updatedTask), 200);
            return;
        }

        // DELETE /tasks/{id}
        if (method.equals("DELETE") && containPathVariable(path)) {
            Long id = getPathVariable(path);
            taskRepository.delete(id);
            sendResponse(exchange, "success", 200);
            return;
        }

        sendResponse(exchange, "지원하지 않는 경로입니다", 404);
    }

    private void sendResponse(HttpExchange exchange, String content, int status) throws IOException {
        exchange.sendResponseHeaders(status , content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    /*
     * path가 올바른 pathvarialbe을 포함하고 있는지 확인하는 메소드
     * 좀 더 좋은 좋은 이름을 지을 수 있을거 같은데 생각이 안남 ㅜㅜ
     */
    private boolean containPathVariable(String path) {

        String substring = path.substring(TASK_DEFAULT_PATH.length());// ex) /tasks/3 -> /3

        if (substring.length() == 0) {
            return false;
        }

        if (substring.charAt(0) != '/') {
            return false;
        }
        
        String pathVariable = substring.substring(1);
        try {
            Long.parseLong(pathVariable);
        } catch (NumberFormatException e) {
            System.out.println(pathVariable + "은 숫자 형식이 아닙니다.");
            return false;
        }

        return true;
    }

    /*
     * containPathVariable 과 중복되는 코드가 많아 리팩토링을 해야 될거같음
     */
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
