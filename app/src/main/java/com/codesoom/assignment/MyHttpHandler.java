package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L; // 여기서 private Long newId 처럼 선언만 하고 정의가 안돼있으면, 아무리 genereateId 해봤자 도루묵

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); //URI에서 path만 얻는다.

        // Body 를 얻는 과정을 하나의 메서드로 만들자. 글고 바디는 post에서만 필요하니까 공통으로 뺄 필요가 없다.
//        InputStream inputStream = exchange.getRequestBody();
//        String body = new BufferedReader(new InputStreamReader(inputStream))
//                .lines()
//                .collect(Collectors.joining("\n"));

        if (path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            handleItem(exchange, method, id);
            return;
        }
        send(exchange, 200, "Hello");
    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);

        if (task == null) {
            send(exchange, 404, "");
            return;
        }

        if (method.equals("GET")) {
            send(exchange, 200, tasksToJSON(task));
        }

        if (method.equals("PUT") || method.equals("PATCH")) {
            String body = getBody(exchange);
            Task source = bodyToTask(body);
            task.setTitle(source.getTitle()); // 번호를 할당하니 POST가 또 오작
            send(exchange, 200, tasksToJSON(task));
        }


        if (method.equals("DELETE")) {
            tasks.remove(task);
            send(exchange, 200, "");
        }
    }

    private Task findTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst().orElse(null); // .get() => .orElse(null)
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            send(exchange, 200, tasksToJSON(tasks));
        }
        if (method.equals("POST")) {
            createTask(exchange); // 객체 지향 메서드 -> 메서드 추
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);
        Task task = bodyToTask(body);
        task.setId(generateId()); // 번호를 할당하니 POST가 또 오작
        tasks.add(task);
        send(exchange, 201, tasksToJSON(task)); // tasks 넣으면 당연히 정확한 id의 body 못찾고, 전체 다 찾아옴
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines() // 여러 줄이 나오는 것을
                .collect(Collectors.joining("\n")); // 제대로 줄 넘김 해서 받음
    }

    private String tasksToJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

    private Task bodyToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    // path의 id값 분리
//    Long getId(String path) {
//        String[] splitPath = path.split("/");
//        return Long.parseLong(splitPath[2]);
//    }
}