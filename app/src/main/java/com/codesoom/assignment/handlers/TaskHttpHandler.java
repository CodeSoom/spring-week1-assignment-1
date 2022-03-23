package com.codesoom.assignment.handlers;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks;

    private static final String PATH = "/tasks";

    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_CREATE = 201;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;

    public TaskHttpHandler() {
        this.tasks = new ArrayList<>();
    }

    private boolean isMatch(String requestURI) {
        final String regex = "^\\/tasks\\/[0-9]+$";
        final Pattern pattern = Pattern.compile(regex);

        final Matcher matcher = pattern.matcher(requestURI);
        return matcher.matches();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestURI = exchange.getRequestURI().getPath();

        System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI());

        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.add("Content-Type", "application/json");

        OutputStream outputStream = exchange.getResponseBody();
        String responseBody = "";
        int httpStatus = 0;

        if (PATH.equals(requestURI)) {
            if (HttpMethod.GET.name().equals(exchange.getRequestMethod())) {
                System.out.println(" - ToDo 전체 조회 요청");

                responseBody = tasksToJSON();
                httpStatus = HTTP_STATUS_OK;
            }

            if (HttpMethod.POST.name().equals(exchange.getRequestMethod())) {
                System.out.println(" - ToDo 추가 요청");

                final String requestBody = getRequestBody(exchange.getRequestBody());
                if (requestBody.isBlank()) {
                    System.out.println(" - ToDo 추가 ERROR: 요청 값이 없음");
                    responseBody = "할 일을 입력해 주세요.";
                    httpStatus = HTTP_STATUS_BAD_REQUEST;

                } else {
                    Task newTask = toTask(requestBody);
                    addTask(newTask);

                    responseBody = taskToJSON(newTask);
                    httpStatus = HTTP_STATUS_CREATE;
                }
            }
        } else if (isMatch(requestURI)) {
            System.out.println("/tasks/{id} 요청");
        } else {
            System.out.println("올바르지 않은 경로 요청");
        }

        exchange.sendResponseHeaders(httpStatus, responseBody.getBytes(StandardCharsets.UTF_8).length);
        outputStream.write(responseBody.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 모든 Task 목록을 JSON 형식으로 반환한다.
     * */
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    /**
     * 하나의 Task 객체를 JSON 형식으로 반환한다.
     * */
    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    /**
     * 요청받은 할 일을 Task 객체로 변환하여 반환한다.
     * */
    private Task toTask(String content) throws JsonProcessingException {
        System.out.println("content = " + content);
        return objectMapper.readValue(content, Task.class);
    }

    /**
     * 요청받은 JSON 형식의 content를 문자열로 반환한다.
     * */
    private String getRequestBody(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 증가된 id를 반환한다.
     * */
    private Long generateId() {
        if (tasks.isEmpty() || tasks == null) {
            return 1L;
        }

        Long maxId = Long.MIN_VALUE;
        for (Task task : tasks) {
            maxId = task.getId() > maxId ? task.getId() : maxId;
        }
        return maxId + 1L;
    }

    /**
     * 새로운 할 일을 추가한다.
     * */
    private void addTask(Task task) {
        tasks.add(task);
    }

}