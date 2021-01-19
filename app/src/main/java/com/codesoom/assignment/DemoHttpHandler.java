package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.JSONParser.taskToJSON;
import static com.codesoom.assignment.JSONParser.toTask;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "";

        System.out.println(method + " " + path);
        if (!body.isBlank()) {
            System.out.println(body);

            Task task = toTask(body, Long.valueOf(tasks.size() + 1));
            tasks.add(task);
        }

        if (method.equals("GET") && path.equals("/tasks")) {
            content = "ToDo 목록 얻기";
        }

        if (method.equals("GET") && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 상세 조회하기";
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = taskToJSON(tasks.get(tasks.size() - 1));
        }

        if ((method.equals("PUT") || method.equals("PATCH")) && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 제목 수정하기";
        }

        if (method.equals("DELETE") && path.matches("/tasks/*[0-9]*")) {
            content = "ToDo 삭제하기";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
