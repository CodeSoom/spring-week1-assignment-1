package com.assignment1.demo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DemoHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String method = exchange.getRequestMethod();
        // 2. path 확인하기
        URI uri = exchange.getRequestURI();
        // path string만 얻기~
        String path = uri.getPath();

        System.out.println(method+ " " + path);

        String content = "안녕! 나는 REST API 연습 중이야!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = "We have no tasks...";
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task!";
        }


        // http status code, content 길이 반환
        exchange.sendResponseHeaders(200, content.getBytes().length );

        OutputStream outputStream = exchange.getResponseBody();
        // content 내용 넘겨주기
        outputStream.write(content.getBytes());

        outputStream.flush();
        outputStream.close();

    }
}
