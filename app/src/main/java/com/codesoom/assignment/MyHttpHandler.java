package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyHttpHandler implements HttpHandler {

    JSONConverter jsonConverter = new JSONConverter();

    private List<Task> tasks = new ArrayList<>();

    public MyHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task1");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpRequestInfo httpRequestInfo = new HttpRequestInfo(exchange);

        if (!httpRequestInfo.getBody().isBlank()) {
            // JSON 데이터를 task로 변환
            Task task = jsonConverter.JSONToTask(httpRequestInfo.getBody());
            tasks.add(task);
        }

        String content = "Hello, world!";

        if (!httpRequestInfo.getMethod().isEmpty()) {
            switch (httpRequestInfo.getMethod()) {
                case "GET":
                    System.out.println(httpRequestInfo.toString());
                    break;
                case "POST":
                    System.out.println(httpRequestInfo.toString());
                    break;
                case "PUT":
                    System.out.println(httpRequestInfo.toString());
                    break;
                case "PATH":
                    System.out.println(httpRequestInfo.toString());
                    break;
                case "DELETE":
                    System.out.println(httpRequestInfo.toString());
                    break;
                default:
                    System.out.println(httpRequestInfo.toString());
                    break;
            }
            exchange.sendResponseHeaders(200, content.length());
        } else {
            content = "There isn't Method";
            exchange.sendResponseHeaders(404, content.length());
        }

        // response 처리
        OutputStream outputStream = exchange.getResponseBody(); // getResponseBody() : Response를 byte 배열로 반환
        outputStream.write(content.getBytes());  // 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보냄
        outputStream.flush(); // 버퍼에 남아있는 데이터를 모두 출력시키고 버퍼를 비움
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌
    }
}