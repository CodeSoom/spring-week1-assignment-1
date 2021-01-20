package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHttpHandler implements HttpHandler {

    JSONConverter jsonConverter = new JSONConverter();
    TaskRepository taskRepository = new TaskRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        HttpRequest httpRequest = new HttpRequest(exchange);

        String content = "Hello, world!";

        if (!httpRequest.hasMethod().isEmpty()) {
            switch (httpRequest.hasMethod()) {
                case "GET":
                    break;
                case "POST":
                    POSTCreateNewTask(exchange, httpRequest);
                    break;
                case "PUT":
                    break;
                case "PATH":
                    System.out.println(httpRequest.toString());
                    break;
                case "DELETE":
                    System.out.println(httpRequest.toString());
                    break;
                default:
                    System.out.println(httpRequest.toString());
                    break;
            }
            exchange.sendResponseHeaders(200, content.length());
        }

        if (httpRequest.hasMethod().isEmpty()) {
            content = "There isn't Method";
            exchange.sendResponseHeaders(404, content.length());
        }

        // response 처리
        OutputStream outputStream = exchange.getResponseBody(); // getResponseBody() : Response를 byte 배열로 반환
        outputStream.write(content.getBytes());  // 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보냄
        outputStream.flush(); // 버퍼에 남아있는 데이터를 모두 출력시키고 버퍼를 비움
        outputStream.close(); // 호출해서 사용했던 시스템 자원을 풀어줌
    }

    private void POSTCreateNewTask(HttpExchange exchange, HttpRequest httpRequest) throws JsonProcessingException {
        String path = httpRequest.hasPath();
        if (path.contains("/tasks")) {
            Task newTask = jsonConverter.JSONToTask(httpRequest.hasBody());
            taskRepository.createNewTask(newTask);
        }
    }

}
