package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Task의 로직을 가지고 있고 관련된 Http 요청을 처리하는 클래스
 */
public class TaskHttpHandler implements HttpHandler {
    private Long id = 0L;
    private final List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (method.equals("GET") && path.equals("/tasks")) {
            sendGetResponse(exchange);
            return;
        }

        if (method.equals("GET") && isDetailMatches(path)) {
            sendDetailGetResponse(exchange, path);
            return;
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            sendPostResponse(exchange);
            return;
        }

        if (method.equals("PUT") && isDetailMatches(path)) {
            sendPutResponse(exchange, path);
            return;
        }

        if (method.equals("DELETE") && isDetailMatches(path)) {

        }

        sendBadResponse(exchange);
    }

    private void sendPutResponse(HttpExchange exchange, String path) throws IOException {
        String request = parsingRequest(exchange.getRequestBody());
        if (request.isBlank()) {
            sendBadResponse(exchange);
        }

        HashMap requestMap = getRequestMap(request);
        Long findId = Long.valueOf(path.split("/")[2]);

        try {
            Task changedTask = taskService.changeTask(findId, (String) requestMap.get("title"));
            String content = taskToJson(changedTask);
            sendGetResponse(exchange);
            writeResponseBody(exchange, content);
        } catch (NoSuchElementException e) {
            sendNotFoundResponse(exchange, 404, -1);
        }
    }

    private void sendNotFoundResponse(HttpExchange exchange, int rCode, int responseLength) throws IOException {
        exchange.sendResponseHeaders(rCode, responseLength);
    }

    /**
     * 숫자 형식의 id를 가진 GET 요청이 왔을 때 id와 같은 task를 찾아 리턴한다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @param path 수신된 경로
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    // try catch 대신 Optional을 사용해봤습니다.
    private void sendDetailGetResponse(HttpExchange exchange, String path) throws IOException {
        String[] splitedPath = path.split("/");
        Optional<Task> storedTask = taskService.getTask(Long.valueOf(splitedPath[2]));

        if (storedTask.isPresent()) {
            String content = taskToJson(storedTask.get());
            sendNotFoundResponse(exchange, 200, content.getBytes().length);
            writeResponseBody(exchange, content);
            return;
        }

        sendNotFoundResponse(exchange, 404, -1);
    }

    /**
     * 요청이 어떤 분기문에도 처리되지 않은 경우 400을 헤더에 저장합니다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    private void sendBadResponse(HttpExchange exchange) throws IOException {
        sendNotFoundResponse(exchange, 400, -1);
    }

    /**
     * GET 요청을 받아 저장된 Tasks를 찾아 리턴한다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    private void sendGetResponse(HttpExchange exchange) throws IOException {
        String content = tasksToJson(taskService.getTasks());
        sendNotFoundResponse(exchange, 200, content.getBytes().length);
        writeResponseBody(exchange, content);
    }

    /**
     * POST 요청을 받아 요청 본문이 비었으면 400을 리턴하고
     * 아니라면 Task 객체를 생성하고 201과 함께 리턴합니다.
     *
     * @param exchange 수신된 요청과 응답을 가진 파라미터
     * @throws IOException 입출력에 문제가 있는 경우 던집니다.
     */
    private void sendPostResponse(HttpExchange exchange) throws IOException {
        String request = parsingRequest(exchange.getRequestBody());

        if (request.isBlank()) {
            sendNotFoundResponse(exchange, 400, -1);
            return;
        }

        HashMap requestMap = getRequestMap(request);
        request = (String) requestMap.get("title");

        String content = taskToJson(taskService.createTask(request));
        sendNotFoundResponse(exchange, 201, content.getBytes().length);
        writeResponseBody(exchange, content);
    }

    /**
     * 요청된 본문을 HashMap으로 변환해서 리턴합니다.
     *
     * @param request 요청된 본문
     * @return 변환된 HashMap 리턴
     * @throws JsonProcessingException Json 변환에 문제가 발생할 경우 던집니다.
     */
    // 타입을 정의하지 않은 HashMap을 던져도 괜찮을까요?
    private HashMap getRequestMap(String request) throws JsonProcessingException {
        return objectMapper.readValue(request, HashMap.class);
    }

    /**
     * 응답할 본문을 받아 응답 본문에 담아 보냅니다.
     *
     * @param exchange 응답을 담을 본문을 가진 파라미터
     * @param content 응답 내용
     * @throws IOException 입출력이 잘못될 경우 던집니다.
     */
    private void writeResponseBody(HttpExchange exchange, String content) throws IOException {
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    /**
     * Task의 숫자 형식의 id가 경로에 포함되어 있으면 true, 아니면 false를 리턴합니다.
     *
     * @param path 수신된 Http 요청의 경로
     * @return 숫자 형식의 id가 경로에 포함되어 있으면 true, 아니면 false
     */
    private boolean isDetailMatches(String path) {
        return path.matches("/tasks/[0-9]+");
    }

    /**
     * 특정 Task를 GET 요청이 왔을 때, 요청한 Task를 리턴한다.
     *
     * @param path 수신된 Http 요청의 경로
     * @return 요청한 Task를 String으로 변환해서 리턴한다.
     * @throws JsonProcessingException Json으로 변환하면서 에러가 발생한 경우 던집니다.
     * @throws NoSuchElementException 요청된 id를 찾지 못했을 경우 던집니다.
     */
    private String handleDetailGet(String path) throws JsonProcessingException, NoSuchElementException {
        String[] splitedPath = path.split("/");
        Long findId = Long.parseLong(splitedPath[2]);

        Task task = tasks.stream()
                    .filter(t -> t.getId().equals(findId))
                    .findFirst()
                    .orElseThrow();

        return taskToJson(task);
    }

    /**
     * DELETE 요청이 왔을 때 경로의 id를 찾아 제거하고 빈 문자열을 리턴한다.
     *
     * @param path 수신된 Http 요청의 경로
     * @return 빈 문자열을 리턴
     * @throws NoSuchElementException 요청된 id를 찾지 못했을 경우 던집니다.
     */
    private String handleDelete(String path) {
        Long findId = Long.valueOf(path.split("/")[2]);

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(findId)) {
                tasks.remove(i);
//                return i;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * POST 요청이 왔을 때 요청 본문이 있으면 path의 id에 맞는 Task를 찾아 변경한 후 리턴, 없으면 잘못된 요청 메시지를 리턴
     *
     * @param path 찾을 Task의 id를 가지고 있는 경로
     * @param request 수신된 요청 본문
     * @return 본문이 있을 경우 만든 변경된 Task 리턴, 없으면 잘못된 요청 메시지 리턴
     * @throws RuntimeException JSon으로 변환할 때, 에러가 발생하면 던집니다.
     * @throws NoSuchElementException 요청된 id를 찾지 못했을 경우 던집니다.
     */
    private String handlePut(String path, String request) {
        if (!request.isBlank()) {
            try {
                String[] splitedPath = path.split("/");
                Long findId = Long.valueOf(splitedPath[2]);

                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(findId))
                        .findFirst()
                        .orElseThrow();

                storedTask.setTitle(
                        (String) objectMapper.readValue(request, HashMap.class)
                                .get("title"));

                return taskToJson(storedTask);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Json으로 변환할 때, 에러가 발생했습니다.");
            }
        }

        return handleBadRequest();
    }

    /**
     * 수신된 Http 요청이 잘못된 경우 응답 본문을 만들어 리턴한다.
     *
     * @return 응답할 본문
     */
    private String handleBadRequest() {
        return "Bad Request";
    }

    /**
     * POST 요청이 왔을 때 요청 본문이 있으면 Task를 만들어 저장한 후 리턴, 없으면 잘못된 요청 메시지를 리턴
     *
     * @param request 수신된 요청 본문
     * @return 본문이 있을 경우 만든 Task 리턴, 없으면 잘못된 요청 메시지 리턴
     * @throws RuntimeException JSon으로 변환할 때, 에러가 발생하면 던집니다.
     */
    private String handlePost(String request) {
        if (!request.isBlank()) {
            try {
                Task task = toTask(request);
                task.setId(id++);

                tasks.add(task);

                Task storedTask = tasks.stream()
                        .filter(t -> t.getId().equals(id - 1))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                return taskToJson(storedTask);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Json으로 변환할 때, 에러가 발생했습니다.");
            }
        }

        return handleBadRequest();
    }

    /**
     * 수신된 Http 요청의 본문을 String으로 변환하여 리턴한다.
     *
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
     *
     * @param content 요청 받은 컨텐트
     * @return 생성한 Task를 리턴
     * @throws JsonProcessingException 요청 받은 컨텐트를 Task로 매핑하지 못햇을 때 던집니다.
     */
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    /**
     * Tasks를 Json String으로 변환하여 리턴
     *
     * @return 변환된 문자열을 리턴
     * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
     **/
    private String tasksToJson(List<Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }

  /**
   * Task를 Json String으로 변환하여 리턴
   *
   * @param task 변환할 Task
   * @return 변환된 문자열을 리턴
   * @throws JsonProcessingException Task를 Json으로 변환하지 못했을 때 던집니다.
   **/
    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
