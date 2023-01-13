package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Rest Api를 만들어 보자.
 * 1. 목록 얻기 - GET /tasks
 * 2. 상세 조회하기 - GET /tasks/{id}
 * 3. 생성 하기 - POST /tasks
 * 4. 제목 수정하기 - PUT/PATCH /tasks/{id}
 * 5. 삭제하기하기 - DELETE /tasks/{id}
 * */
public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    public DemoHttpHandler(){

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (!body.isBlank()){
            Task task = toTask(body);
            tasks.add(task);
        }

        String content = "Hello, world!";
        if (method.equals("GET") && path.equals("/tasks")){
            System.out.println("Get list.");
            content = tasksToJSON();
        }

        String[] pathArr = path.split("/");

        //todo 2번째 path가 숫자가 아닌 문자로 들어올 시 예외처리
        if(method.equals("GET") && pathArr.length == 3 &&
                pathArr[1].equals("tasks")){
            System.out.println("test");
            Long id = Long.valueOf(pathArr[2]);
            System.out.println(id);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        
        return outputStream.toString();
    }
}
