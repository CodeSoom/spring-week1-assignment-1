package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private static String content = "핸들링되지 않은 엔드포인트입니다.";

    private DemoService demoService = new DemoService();
    private List<Task> tasks;
    private Parser parser = new Parser();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean result = false;
        int statusCode = 404;
        final int HAS_PARAMS_VALUE = 7;

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
            tasks = this.demoService.readAllTasks();
            content = parser.toJSON(tasks);
            result = true;
        }

        if (httpMethod.equals("POST") && uri.equals("/tasks")) {
            System.out.println("ToDo 생성하기");
            Task task = this.demoService.createTask(body);
            content = parser.toJSON(task);
            result = true;
        }

        if (uri.length() > HAS_PARAMS_VALUE) { // tasks/{id}
            Long param = extractParams(uri);

            if (httpMethod.equals("GET")) {
                System.out.println("ToDo 상세 조회하기");
                Task task = this.demoService.readTaskById(param);
                content = parser.toJSON(task);
                result = true;
            }

            if (httpMethod.equals("PUT") || httpMethod.equals("PATCH")) {
                System.out.println("ToDo 제목 수정하기");
                result = true;
            }

            if (httpMethod.equals("DELETE")) {
                System.out.println("ToDo 삭제하기");
                result = true;
            }
        }

        statusCode = result ? 200 : 404;

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Long extractParams(String uri) {
        String param = uri.split("/")[2];
        return Long.parseLong(param);
    }

}
