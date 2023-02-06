package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private static String content = "핸들링되지 않은 엔드포인트입니다.";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean result = false;
        int rCode = 404;

        String httpMethod = exchange.getRequestMethod(); // http 메서드
        String uri = exchange.getRequestURI().getPath(); // URI
        InputStream requestBody = exchange.getRequestBody(); // TODO: 이해하기
        String body = new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(httpMethod + " " + uri);
        if (!body.isBlank()) {
            System.out.println(body);
        }

        // 요구사항 구현
        if (httpMethod.equals("GET") && uri.equals("/tasks")) {
            System.out.println("ToDo 목록 얻기");
            result = true;
        }

        if (httpMethod.equals("POST") && uri.equals("/tasks")) {
            System.out.println("ToDo 생성하기");
            result = true;
        }

        rCode = result ? 200 : 404;

        exchange.sendResponseHeaders(rCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
