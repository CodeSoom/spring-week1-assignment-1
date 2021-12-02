package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private static Long autoId = 0L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod(); //요청받은 http method
        URI uri = exchange.getRequestURI();  //요청받은 uri
        String path = uri.getPath(); //요청받은 path
        String content = null; //응답 body
        int code = 200; //응답 코드

        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        String body = new BufferedReader(inputStreamReader)
                .lines()
                .collect(Collectors.joining("\n")); //요청받은 body

        //서버 콘솔 출력
        System.out.println(method + " " + path);

        if (!body.isBlank()) {
            System.out.println(body);
        }

        //클라이언트에서 받은 요청 처리
        if (method.equals("GET") && path.equals("/")) {
            content = "Hello, World!";

        } else if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();

        } else if (method.equals("GET") && path.contains("/tasks/")) {
            Long id = getId(path);
            Task task = getTaskById(id);
            content = toJSON(task);

        } else if (method.equals("POST") && path.equals("/tasks")) {
            Task task = insertTask(body);
            content = toJSON(task);
            code = 201;

        } else if ("PATCH, PUT".contains(method) && path.contains("/tasks/")) {
            Long id = getId(path);
            Task task = updateTask(id, body);
            content = toJSON(task);

        } else if (method.equals("DELETE") && path.contains("/tasks/")) {
            Long id = getId(path);
            deleteTask(id);

        } else {
            code = 404;
            content = "Not Found";
        }

        //서버에서 클라이언트로 응답 처리
        int responseBodyLength = content != null ? content.getBytes().length : 0;
        byte[] responseBody = content != null ? content.getBytes() : new byte[0];

        exchange.sendResponseHeaders(code, responseBodyLength);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody);
        outputStream.flush();
        outputStream.close();
    }

    //==편의 메서드==//

    /**
     * 클라이언트에서 받은 path에서 Task의 id 얻기
     */
    private Long getId(String path) {
        String[] split = path.split("/tasks/");
        return Long.valueOf(split[1]);
    }

    //==json 변환 메서드==//

    /**
     * Task를 JSON으로 포맷하기
     */
    private String toJSON(Task task) {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            objectMapper.writeValue(outputStream, task);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toString();
    }

    /**
     * json을 Task로 포맷하기
     */
    private Task toTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    /**
     * Task 컬렉션에서 Task의 id에 해당하는 객체 얻기
     */
    private Task getTaskById(Long id) {
        Task result = null;

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                result = task;
                break;
            }
        }

        return result;
    }

    /**
     * Task 컬렉션을 json으로 포맷하기
     */
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    //==CRUD 메서드==//

    /**
     * Task 저장
     */
    private Task insertTask(String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(++autoId);
        tasks.add(task);
        return task;
    }

    /**
     * Task 수정
     */
    private Task updateTask(Long id, String body) throws JsonProcessingException {

        Task findTask = toTask(body);

        Task result = null;

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                task.setTitle(findTask.getTitle());
                result = task;
                break;
            }
        }

        return result;
    }

    /**
     * Task 삭제
     */
    private void deleteTask(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                tasks.remove(task);
                break;
            }
        }
    }
}
