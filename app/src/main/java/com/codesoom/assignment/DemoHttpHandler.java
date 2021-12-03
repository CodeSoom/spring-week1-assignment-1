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
import java.util.Optional;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>(); //할일 목록
    private static Long autoId = 0L; //Task에서 자동증가한 id의 마지막 값

    private final ObjectMapper objectMapper = new ObjectMapper(); //jackson 라이브러리 클래스

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod(); //요청받은 http method
        final URI uri = exchange.getRequestURI();  //요청받은 uri
        final String path = uri.getPath(); //요청받은 path

        //서버 콘솔 출력
        System.out.printf("%s %s%n", method, path);

        String responseBody = null; //응답 body
        int code = 200; //응답 코드

        String json = resolveRequestBody(exchange);

        //클라이언트에서 받은 요청 처리
        if (method.equals("GET") && path.equals("/")) {
            responseBody = "Hello, World!";

        } else if (method.equals("GET") && path.equals("/tasks")) {
            responseBody = tasksToJSON();

        } else if (method.equals("GET") && path.contains("/tasks/")) {
            try {
                Long id = getId(path);
                Optional<Task> taskOptional = findTaskById(id);

                if (taskOptional.isPresent()) {
                    responseBody = toJSON(taskOptional.get());
                }

            } catch (NumberFormatException e) {
                code = 400;
                responseBody = "Bad Request";
            }

        } else if (method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(json);
            insertTask(task);
            responseBody = toJSON(task);
            code = 201;

        } else if ("PATCH, PUT".contains(method) && path.contains("/tasks/")) {
            try {
                Long id = getId(path);
                Task taskRequest = toTask(json);
                Optional<Task> taskOptional = updateTask(id, taskRequest);

                if (taskOptional.isPresent()) {
                    Task task = taskOptional.get();
                    responseBody = toJSON(task);
                }

            } catch (NumberFormatException e) {
                code = 400;
                responseBody = "Bad Request";
            }

        } else if (method.equals("DELETE") && path.contains("/tasks/")) {
            try {
                Long id = getId(path);
                deleteTask(id);

            } catch (NumberFormatException e) {
                code = 400;
                responseBody = "Bad Request";
            }

        } else {
            code = 404;
            responseBody = "Not Found";
        }

        resolveResponse(exchange, responseBody, code);
    }

    /**
     * 클라이언트의 요청 body를 해결해 주는 메서드
     */
    private String resolveRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        String json = new BufferedReader(inputStreamReader)
                .lines()
                .collect(Collectors.joining("\n")); //요청받은 body

        if (!json.isBlank()) {
            System.out.println(json);
        }

        return json;
    }

    //==편의 메서드==//

    /**
     * 클라이언트에서 받은 path에서 Task의 id 얻기
     */
    private Long getId(String path) {
        String[] split = path.split("/tasks/");
        return Long.valueOf(split[1]);
    }

    /**
     * 클라이언트에 응답을 해결해 줄 메서드
     */
    private void resolveResponse(HttpExchange exchange, String content, int code) throws IOException {
        final int responseBodyLength = content != null ? content.getBytes().length : 0;
        final byte[] responseBody = content != null ? content.getBytes() : new byte[0];

        exchange.sendResponseHeaders(code, responseBodyLength);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody);
        outputStream.flush();
        outputStream.close();
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
     * 할일(Task) 목록에서 주어인 아이디를 가진 할일(Task)을 찾기
     */
    private Optional<Task> findTaskById(Long id) {
        Optional<Task> result = Optional.empty();

        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                result = Optional.of(task);
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
    private void insertTask(Task task) {
        task.setId(++autoId);
        tasks.add(task);
    }

    /**
     * Task 수정
     */
    private Optional<Task> updateTask(Long id, Task taskRequest) {
        Optional<Task> taskOptional = findTaskById(id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskRequest.getTitle());
        }

        return taskOptional;
    }

    /**
     * Task 삭제
     */
    private void deleteTask(Long id) {
        findTaskById(id).ifPresent(tasks::remove);
    }
}
