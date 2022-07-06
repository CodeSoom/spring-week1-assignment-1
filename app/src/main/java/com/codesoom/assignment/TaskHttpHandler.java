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
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String content = "Basic";
        String request = parsingRequest(exchange.getRequestBody());

        if (method.equals("GET")) {
            content = tasksToJson();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if (method.equals("POST") && path.equals("/tasks")) {
            content = handlePost(request);
            exchange.sendResponseHeaders(201, content.getBytes().length);
        } else if (method.equals("PUT") && path.matches("/tasks/[0-9]+")) {
            content = handlePut(path, request);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if (method.equals("DELETE")) {

        } else {
            content = handleBadRequest();
            exchange.sendResponseHeaders(400, content.getBytes().length);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    /**
     * POST 요청이 왔을 때 요청 본문이 있으면 path의 id에 맞는 Task를 찾아 변경한 후 리턴, 없으면 잘못된 요청 메시지를 리턴
     * @param path 찾을 Task의 id를 가지고 있는 경로
     * @param request 수신된 요청 본문
     * @return 본문이 있을 경우 만든 변경된 Task 리턴, 없으면 잘못된 요청 메시지 리턴
     * @throws RuntimeException JSon으로 변환할 때, 에러가 발생하면 던집니다.
     */
    private String handlePut(String path, String request) {
        String[] splitedPath = path.split("/");
        Long findId = Long.valueOf(splitedPath[2]);
        String content;

        if (!request.isBlank()) {
            try {
                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(findId))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                Task taskToChange = toTask(request);
                storedTask.setTitle(taskToChange.getTitle());

                content = taskToJson(storedTask);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Json으로 변환할 때, 에러가 발생했습니다.");
            }
        } else {
            content = handleBadRequest();
        }

        return content;
    }

    /**
     * 수신된 Http 요청이 잘못된 경우 응답 본문을 만들어 리턴한다.
     * @return 응답할 본문
     */
    private String handleBadRequest() {
        return "Bad Request";
    }

    /**
     * POST 요청이 왔을 때 요청 본문이 있으면 Task를 만들어 저장한 후 리턴, 없으면 잘못된 요청 메시지를 리턴
     * @param request 수신된 요청 본문
     * @return 본문이 있을 경우 만든 Task 리턴, 없으면 잘못된 요청 메시지 리턴
     * @throws RuntimeException JSon으로 변환할 때, 에러가 발생하면 던집니다.
     */
    private String handlePost(String request) {
        String content;
        
        if (!request.isBlank()) {
            try {
                Task task = toTask(request);
                task.setId(id++);

                tasks.add(task);

                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(id - 1))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                content = taskToJson(storedTask);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Json으로 변환할 때, 에러가 발생했습니다.");
            }
        } else {
            return handleBadRequest();
        }

        return content;
    }

    /**
     * 수신된 Http 요청의 본문을 String으로 변환하여 리턴한다.
     * @param requestBody 수신된 Http 요청의 본문
     * @return 본문을 String으로 변환하여 리턴
     */
    private String parsingRequest(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 요청 받은 컨텐트를 Task로 매핑하여 리턴한다.
     * @param content 요청 받은 컨텐트
     * @return 생성한 Task를 리턴
     * @throws JsonProcessingException 요청 받은 컨텐트를 Task로 매핑하지 못햇을 때 던집니다.
     */
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    /**
     * Tasks를 Json String으로 변환하여 리턴
     * @return 변환된 문자열을 리턴
     * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
     **/
    private String tasksToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }

  /**
   * Task를 Json String으로 변환하여 리턴
   * @param task 변환할 Task
   * @return 변환된 문자열을 리턴
   * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
   **/
    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
