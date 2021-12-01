package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

//    /*생성자 추가*/
//    public DemoHttpHandler() {
//        Task task = new Task();
//        task.setId(1L);
//        task.setTitle("과제 제출하기");
//
//        tasks.add(task);
//    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        // 추가 시 / 숨겨져 있지만 포함 된거랑 비슷
        String path = uri.getPath();



        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method+ "" + path);
        if(!body.isBlank()) {
            System.out.println(body);

            Task task = toTask(body);
            tasks.add(task);

            System.out.println(task);
        }


        String content = "Hello, World";

        if ("GET".equals(method) && path.equals("/tasks")) {
            content = taskToJson();
        }

//        if ("GET".equals(method) && path.equals("/tasks/1")) {
//            content = taskToJson();
//        }

        if ("POST".equals(method) && path.equals("/tasks")) {
                content = "Create a new task";
        }

//        if ("PUT".equals(method) || "PATCH".equals(method)) {
//            if (path.equals("/tasks/1")) {
//                content = "{id:1, title:과제 제출하기}";
//            }
//        }

//        if ("DELETE".equals(method) && path.equals("/tasks/1")) {
//
//        }

        // content 길이가 0 일 때 Transfer-encoding: chunked 메시지가 나옴
        exchange.sendResponseHeaders(200, content.getBytes().length);

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
}

/*상태 코드*/
/*
* 200 : OK
* 201 : Create
* 204 : No Content
* 404 : Not Found
*
* */

/*path URI은 주소를 통으로 써야 하기 때문에 정규표현식으로 쪼개야 한다.*/
