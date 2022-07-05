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

// Task에 대한 응답과 요청을 처리하는 핸들러
public class TaskHttpHandler implements HttpHandler {
    public static final String NOT_FOUND = "404 Not Found";
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String content = null;
        String request = parsingRequest(exchange);

        if (method.equals("GET")) {
            content = tasksToJson();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if (method.equals("POST")) {
            if (!request.isBlank()) {
                Task task = toTask(request);
                tasks.add(task);
                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(id - 1))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("생성 과정 중 오류가 발생했습니다."));
                content = taskToJson(storedTask);
                exchange.sendResponseHeaders(201, content.getBytes().length);
            }
        } else if (method.equals("PUT") && path.matches("/tasks/[0-9]+")) {
            String[] splitedPath = path.split("/");
            Long findId = Long.valueOf(splitedPath[2]);

            if (!request.isBlank()) {
                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(findId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("찾을 수 없는 Task입니다."));
                storedTask.setTitle("변경되었습니당.");
                content = taskToJson(storedTask);
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else {
                content = "올바른 Request 요청이 아닙니다.";
                exchange.sendResponseHeaders(400, content.getBytes().length);
            }
        } else if (method.equals("DELETE")) {

        } else {
            content = "Bad Request";
            exchange.sendResponseHeaders(400, content.getBytes().length);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }
    // 요청온 requestBody를 파싱해주는 기능
    private String parsingRequest(HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    // 요청온 경로가 유효한지 확인해서 boolean 반환
    private boolean validPath(String path) {
        return path.equals("/tasks");
    }

    // 요청 받은 content를 Task 객체로 매핑하고 리턴
    private Task toTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(id++);
        return task;
    }
    // 저장되어 있는 tasks을 Json으로 변환하는 메서드
    private String tasksToJson() throws IOException {
        return objectMapper.writeValueAsString(tasks);
    }
  /**
   * Task를 Json String으로 변환하여 리턴합니다.
   * 
   * @param task 변환할 Task
   * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
   * @return 변환된 문자열을 리턴합니다.
   **/
    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
