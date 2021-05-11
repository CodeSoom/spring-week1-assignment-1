package com.codesoom.assignment.handler;

import com.codesoom.assignment.task.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

public class ServerHttpHandler implements HttpHandler {

    // Task, Handler 설정
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    private long idCount = 1L;


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // InputStream 설정
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        // Response 내용 초기화
        int response = HttpStatusCode.BadRequest.getCode();

        // 출력 내용 초기화
        String content = "";

        // GET method 설정
        if (method.equals("GET") && path.matches("/tasks")) {
            content = tasksToJSON();
            response = HttpStatusCode.OK.getCode();

        } else if(method.equals("GET") && path.contains("/tasks/")) {

            Long num = checkID(path);

            if(tasks.stream().anyMatch((t) -> t.getId() == num)) {
                Task task = tasks.stream()
                        .filter((t) -> t.getId() == num)
                        .findFirst()
                        .get();

                response = HttpStatusCode.OK.getCode();
                content = tasksToJSON(task);
            } else {
                content = "적합한 내용이 없습니다.";
                response = HttpStatusCode.NotFoundError.getCode();
            }

        } else if (method.equals("POST") && path.matches("/tasks") && !body.isBlank()) {
            Task task = toTask(body);
            task.setId(idCount);
            idCount++;

            tasks.add(task);

            content = tasksToJSON(task);
            response = HttpStatusCode.Created.getCode();

        } else if ((method.equals("PUT") || method.equals("PATCH")) && path.contains("/tasks/") && !body.isBlank()) {

            Long num = checkID(path);

            if(tasks.stream().anyMatch((t) -> t.getId() == num)) {
                Task task = tasks.stream()
                        .filter((t) -> t.getId() == num)
                        .findFirst()
                        .get();

                content = taskChange(task, toTask(body).getTitle());
                response = HttpStatusCode.OK.getCode();
            } else {
                content = "적합한 내용이 없습니다.";
                response = HttpStatusCode.NotFoundError.getCode();
            }

        } else if(method.equals("DELETE") && path.contains("/tasks/")) {

            Long num = checkID(path);

            if(tasks.stream().anyMatch((t) -> t.getId() == num)) {
                Task task = tasks.stream()
                        .filter((t) -> t.getId() == num)
                        .findFirst().get();

                tasks.remove(task);
                response = HttpStatusCode.NoContent.getCode();
            } else {
                content = "적합한 내용이 없습니다.";
                response = HttpStatusCode.NotFoundError.getCode();
            }
        } else {
            content = "잘못된 접근 입니다.";
        }

        // 통신 결과 보고
        exchange.sendResponseHeaders(response, content.getBytes().length);

        // OutputStream 내용 지정, 버퍼 지우기, 닫기
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    String taskChange(Task task, String temp) throws IOException {

        task.setTitle(temp);

        return tasksToJSON(task);
    }



    Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    String tasksToJSON(Task task) throws IOException {

        if(task == null) { return "[]"; }

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();

    }

    String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        if (!outputStream.toString().isBlank()) {
            return outputStream.toString();
        } else { return "[]"; }

    }

    Long checkID(String path) {
        return parseLong(path.substring("/tasks/".length()));
    }

    private void doGET() {

    }

}
