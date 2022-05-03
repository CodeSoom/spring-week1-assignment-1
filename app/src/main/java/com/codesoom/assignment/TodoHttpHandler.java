package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>(); // 새로운 task가 생성되면 리스트에 저장됨
    private ObjectMapper objectMapper = new ObjectMapper();
    private static Long id = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod(); // HTTP 요청 메서드(GET, POST, PUT, PATCH, DELETE)
        URI uri = exchange.getRequestURI(); // ex. /
        String path = uri.getPath(); // ex. /tasks
        InputStream inputStream = exchange.getRequestBody();

        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n")); // 여러 줄을 개행해서 받아옴

        String content = "";

        // 할 일 목록 얻기
        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON(); // 리스트에 저장되어 있던 task들을 json 형태로 가져오기
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        // OutputStream 객체 생성 후 write()을 해줘야 클라이언트 측에 응답 내용이 전달됨
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    // tasks 리스트를 json 형태로 반환
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
