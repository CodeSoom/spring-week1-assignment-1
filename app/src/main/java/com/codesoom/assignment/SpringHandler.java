package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SpringHandler implements HttpHandler {
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

        System.out.println(method + " " + path);

        String content = "매일 매일 달리지기 위한 첫걸음 시작하기!";



        if(method.equals("GET") && path.equals("/tasks")) {

            content = tasksToJSON();
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }


        exchange.sendResponseHeaders(httpStatus, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private String tasksToJSON() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        
        return outputStream.toString();
    }
}
