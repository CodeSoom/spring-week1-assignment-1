package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpringHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    static final int httpStatus = 200;

    private List<Task> tasks = new ArrayList<>();

    public SpringHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing....");

        tasks.add(task);

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

        System.out.println(method + " " + path);

        if( !body.isBlank()) {
            System.out.println(body);

            Task task = toTask(body);
            System.out.println(task);
        }

        System.out.println(body);

        String content = "매일 매일 달리지기 위한 첫걸음 시작하기!";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            content = "과제를 생성했습니다.";
        }

        exchange.sendResponseHeaders(httpStatus, content.getBytes().length);

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
