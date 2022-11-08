package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 11월 첫째주 과제
 * ToDo 목록 얻기 - GET /tasks
 * ToDo 상세 조회하기 - GET /tasks/{id}
 * ToDo 생성하기 - POST /tasks
 * ToDo 제목 수정하기 - PUT/PATCH /tasks/{id}
 * ToDo 삭제하기 - DELETE /tasks/{id}
 */
public class TaskHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new JsonMapper();

    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod(); //GET, POST, PUT/PATCH, DELETE...

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        System.out.println("body = " + body);

        String content = "";

        if (requestMethod.equals("GET")) {
            content = tasksToJson();
        }

        if (requestMethod.equals("POST") && !body.isBlank()) {
            System.out.println("requestMethod.equals(\"POST\") && !body.isBlank()");
            Task task = contentToTask(body);
            tasks.add(task);
            content = "Create a new Task";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    /**
     * requestBody에서 받아온 content를 Task 자바 객체로 변환
     * @param content
     * @return
     * @throws JsonProcessingException
     */
    private Task contentToTask(String content) throws JsonProcessingException {
        Task newTask = objectMapper.readValue(content, Task.class);
        return newTask;
    }
    
    /**
     * 자바 객체인 Task를 Json 형태로 변환
     * @return
     * @throws IOException
     */
    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

}
