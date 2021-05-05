package com.codesoom.assignment.handler;

import com.codesoom.assignment.task.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class ServerHttpHandler implements HttpHandler {

    // Task, Handler 설정
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    private int idCount = 1;


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // InputStream 설정
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        // Response 내용 초기화
        int response = 400;

        // 출력 내용 초기화
        String content = "";

        // GET method 설정
        if (method.equals("GET") && path.matches("/tasks")) {
            content = tasksToJSON();
            response = 200;
        }

        // GET method 상세조회 설정
        // 문자열 매칭, 후 마지막 매칭 지점에서 부터 숫자 확인(9번까지만), 그 후 숫자 따오기
        else if(method.equals("GET") && path.contains("/tasks/")) {

            int num = parseInt(path.substring("/tasks/".length()));

            Task task = tasks.stream()
                    .filter((t) -> t.getId() == num)
                    .findFirst()
                    .get();

            response = 200;
            content = tasksToJSON(task);
        }

        // POST method 설정
        else if (method.equals("POST") && path.matches("/tasks") && !body.isBlank()) {
            Task task = toTask(body);
            task.setId(idCount);
            idCount++;

            tasks.add(task);

            content = tasksToJSON();
            response = 201;
        }

        // PUT method 설정
        else if ((method.equals("PUT") || method.equals("PATCH")) && path.contains("/tasks/") && !body.isBlank()) {

            int num = parseInt(path.substring("/tasks/".length()));

            System.out.println(tasks);

            Task task = tasks.stream()
                    .filter((t) -> t.getId() == num)
                    .findFirst()
                    .get();

            System.out.println(task);



            content = taskChange(task, toTask(body).getTitle());

            response = 200;

        } else {
            System.out.println("ERROR");
        }

        // 통신 결과 보고
        exchange.sendResponseHeaders(response, content.getBytes().length);

        // OutputStream 내용 지정, 버퍼 지우기, 닫기
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    String taskChange(Task task, String temp) throws IOException {

        System.out.println(temp);

        task.setTitle(temp);

        return tasksToJSON(task);
    }



    Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    String tasksToJSON(Task task) throws IOException {

        if(task == null) { return "[]"; }

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();

    }

    String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        if (!outputStream.toString().isBlank()) {
            return outputStream.toString();
        } else { return "[]"; }

    }

}
