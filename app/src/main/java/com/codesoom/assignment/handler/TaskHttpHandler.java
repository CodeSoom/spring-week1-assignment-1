package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.HttpResponse;
import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.exception.WrongTaskJsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.domain.HttpMethod.*;
import static java.nio.charset.StandardCharsets.*;


public class TaskHttpHandler implements HttpHandler {

    private final List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String httpMethod = exchange.getRequestMethod();
        InputStream httpBodyInputStream = exchange.getRequestBody();
        String httpBody = parseInputStream(httpBodyInputStream);

        try {
            response(exchange, handleTask(httpMethod, httpBody));
        } catch (WrongTaskJsonException e) {
            response(exchange, new HttpResponse(e.getMessage(), 400));
        } catch (Exception e) {
            response(exchange, new HttpResponse(e.getMessage(), 500));
        }
    }

    private void response(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getContent().getBytes(UTF_8).length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(httpResponse.getContent().getBytes(UTF_8));
        responseBody.flush();
        responseBody.close();
    }

    private HttpResponse handleTask(String method, String body) {
        // TODO: Task 단일 조회
        // TODO: Task 수정
        // TODO: Task 삭제

        switch (method) {
            // Task 생성
            case POST -> {
                Task task = parseJsonToTask(body);

                tasks.add(task);
                return new HttpResponse(parseTaskToJson(task), 201);
            }
            // Task 목록 조회
            case GET -> {
                return new HttpResponse(parseTasksToJson(tasks), 200);
            }
        }

        return new HttpResponse("지원하지 않는 메서드입니다.", 405);
    }

    private String parseTaskToJson(Task task) {
        try {
            return objectMapper.writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Task 객체를 JSON 문자열로 변환하는데 실패하였습니다.");
        }
    }

    private String parseTasksToJson(Collection<Task> tasks) {
        try {
            return objectMapper.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Tasks 컬렉션 객체를 JSON 문자열로 변환하는데 실패하였습니다.");
        }
    }

    private Task parseJsonToTask(String body) {
        try {
            return objectMapper.readValue(body, Task.class);
        } catch (JsonProcessingException e) {
            throw new WrongTaskJsonException("잘못된 JSON 형식입니다. ex) { \"title\": \"과제 제출하기\" }");
        }
    }

    /**
     * InputStream 을 파싱하여 문자열로 반환한다.
     * @param inputStream 파싱될 InputStream
     * @return parsedString 파싱된 InputStream
     */
    private String parseInputStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
