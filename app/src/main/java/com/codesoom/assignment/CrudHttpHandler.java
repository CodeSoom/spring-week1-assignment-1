package com.codesoom.assignment;

import com.codesoom.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrudHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private Long autoId = 1L; // 아이디를 자동으로 부여하기 위한 변수


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        OutputStream outputStream = exchange.getResponseBody();

        String response = "[]";

        if(method.equals("GET") && path.equals("/task")) {
            if(!tasks.isEmpty()) {
                response = toTaskJson();
            }
            exchange.sendResponseHeaders(200,response.getBytes().length);
        }
        else if(method.equals("POST") && path.equals("/task")) {

            InputStream inputStream = exchange.getRequestBody();
            String content = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

            Task task = toTask(content);
            task.setId(autoId++);
            tasks.add(task);

            response=toTaskJson();
            exchange.sendResponseHeaders(201,response.getBytes().length);
        }
        else{
            exchange.sendResponseHeaders(200, 0);
        }

        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content,Task.class);
    }

    private String toTaskJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream,tasks);

        return outputStream.toString();
    }

}
