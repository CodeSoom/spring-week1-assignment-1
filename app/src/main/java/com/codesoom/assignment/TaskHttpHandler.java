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

        String content = "Basic";
        String request = parsingRequest(exchange.getRequestBody());

        if (method.equals("GET")) {
            content = tasksToJson();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if (method.equals("POST") && path.equals("/tasks")) {
            content = handlePost(content, request);
            exchange.sendResponseHeaders(201, content.getBytes().length);
        } else if (method.equals("PUT") && path.matches("/tasks/[0-9]+")) {
            String[] splitedPath = path.split("/");
            Long findId = Long.valueOf(splitedPath[2]);

            if (!request.isBlank()) {
                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(findId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("찾을 수 없는 Task입니다."));

                Task taskToChange = toTask(request);
                storedTask.setTitle(taskToChange.getTitle());

                content = taskToJson(storedTask);
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else {
                content = "올바른 Request 요청이 아닙니다.";
                exchange.sendResponseHeaders(400, content.getBytes().length);
            }
        } else if (method.equals("DELETE")) {

        } else {
            content = sendBadRequest(exchange);
        }

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    /**
     * 수신된 Http 요청이 잘못된 경우 에러 코드를 만들고 응답 본문을 리턴한다.
     * @param exchange 수신된 Http 요청을 가진 파라미터
     * @return 응답할 본문
     * @throws IOException 응답 헤더가 이미 전송되었거나 I/O에 문제가 있으면 던집니다.
     */
    private String sendBadRequest(HttpExchange exchange) throws IOException {
        String content = "Bad Request";
        exchange.sendResponseHeaders(400, content.getBytes().length);
        return content;
    }

    private String handlePost(String content, String request) {
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
                throw new RuntimeException("요청이 처리되지 않았습니다.");
            }
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
