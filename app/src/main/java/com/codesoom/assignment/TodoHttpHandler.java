package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TodoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private TodoService todoService = new TodoService();

    public TodoHttpHandler() {

    }


    // String 타입의 문자를 Long 타입으로 변환하여 반환한다.
    public Long convertStringToLong(String str) {
        Long number;
        try {
            number = Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
        return number;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. GET /tasks
        // 2. GET /tasks/{id}
        // 3. POST /tasks
        // 4. PUT/PATCH /tasks/{id}
        // 5. DELETE /tasks/{id}

        URI uri = exchange.getRequestURI();
        String method = exchange.getRequestMethod();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("/n"));
        Task requestBody = new Task();


        String[] splitedPath = path.split("/");
        String response  = "";

        if(!body.isBlank()) {
            requestBody = toTask(body);
        }

        if("GET".equals(method)) {
            if(splitedPath.length == 2 && splitedPath[0].equals("") && splitedPath[1].equals("tasks")) {
                List<Task> tasks = todoService.findAllTasks();
                response = tasksToJson(tasks);
            } else if(splitedPath.length == 3 && splitedPath[0].equals("") && splitedPath[1].equals("tasks") && convertStringToLong(splitedPath[2]) != null) {
                Optional<Task> task = todoService.findTaskById(convertStringToLong(splitedPath[2]));
                if(task.isPresent()) response = taskToJson(task.get());
                else response = "없는 Task입니다!";
            }

        } else if("POST".equals(method)) {
            if(splitedPath.length == 2 && splitedPath[0].equals("") && splitedPath[1].equals("tasks")) {
                Task task = todoService.saveTask(requestBody);
                response = taskToJson(task);
            }

        } else if("PUT".equals(method) || "PATCH".equals(method)) {

        } else if("DELETE".equals(method)) {

        }




        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String tasksToJson(List<Task> tasks) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

}
