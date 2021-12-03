package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        InputStream inputStream = exchange.getRequestBody();

        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        System.out.println(method + "" + path);

        if (!body.isBlank()) {
            System.out.println(body);

            Task task = toTask(body);
            tasks.add(task);

            System.out.println(task);
        }

        String content = " ";
        HttpStatus httpStatus = HttpStatus.HTTP_NOT_FOUND;
        //path 부분은 우선 하드 코딩으로 대체 했습니다. String 타입을 쪼개는 방법을 찾으면 변경 할 예정 입니다.

        //path 주소에 따라 content 값이 다르게 나와야 합니다.
        if ("GET".equals(method) && "/tasks".equals(path)) {
            content = taskToJson();
            httpStatus = HttpStatus.HTTP_OK;
        }
        else if
            ("GET".equals(method) && "/tasks/1".equals(path)) {
            content = taskToJson();
            httpStatus = HttpStatus.HTTP_OK;
        };

        // 상태 코드 201 나오도록 해야 합니다.
        if ("POST".equals(method) && "/tasks".equals(path)) {
            content = taskToJson();
            httpStatus = HttpStatus.HTTP_CREATE;
        }

        // || 보다는 && 이 우선이라 "PUT"과 "PATCH"를 () 안에 묶었습니다.
        if (("PUT".equals(method) || "PATCH".equals(method)) && "/tasks/1".equals(path)) {
            content = taskToJson();
            httpStatus = HttpStatus.HTTP_OK;
        }

        // content 길이가 0 일 때 Transfer-encoding: chunked 메시지가 나옵니다.
        if ("DELETE".equals(method) && "/tasks/1".equals(path)) {
            content= "";
            httpStatus = HttpStatus.HTTP_OK;
        }

        exchange.sendResponseHeaders(httpStatus.getStatus(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }


    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("과제 제출하기");

        tasks.add(task);
    }

}
