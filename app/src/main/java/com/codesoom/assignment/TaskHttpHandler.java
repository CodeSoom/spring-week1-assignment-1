package com.codesoom.assignment;

import com.codesoom.assignment.mapper.TaskMapper;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  Task의 로직을 가지고 있고 관련된 Http 요청을 처리하는 클래스
 */
public class TaskHttpHandler implements HttpHandler {
    private final TaskMapper taskMapper = new TaskMapper();
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
            sendDeleteResponse(exchange, path);
            return;
        }

        sendResponse(exchange, 400, -1);
    }

    private void sendDeleteResponse(HttpExchange exchange, String path) throws IOException {
        try {
            taskService.deleteTask(extractId(path.split("/")));
            sendResponse(exchange, 204, -1);
        } catch (NoSuchElementException e) {
            sendResponse(exchange, 404, -1);
        }
    }

    /**
     * 나누어진 path에서 Id에 해당하는 값을 가져와 숫자 형식으로 변환해서 리턴합니다.
     *
     * @param path 특정 문자로 나누어진 경로
     * @return 경로를 숫자로 변환해 리턴
     */
    private Long extractId(String[] path) {
        return Long.valueOf(path[2]);
    }

    private void sendPutResponse(HttpExchange exchange, String path) throws IOException {
        String request = parsingRequest(exchange.getRequestBody());
        if (request.isBlank()) {
            sendResponse(exchange, 400, -1);
        }

        HashMap requestMap = taskMapper.getRequestMap(request);
        Long findId = extractId(path.split("/"));

        try {
            Task changedTask = taskService.changeTask(findId, (String) requestMap.get("title"));
            String content = taskMapper.taskToJson(changedTask);
            sendResponse(exchange, 200, content.getBytes().length);
            writeResponseBody(exchange, content);
        } catch (NoSuchElementException e) {
            sendResponse(exchange, 404, -1);
        }
    }

    private void sendResponse(HttpExchange exchange, int rCode, int responseLength) throws IOException {
        exchange.sendResponseHeaders(rCode, responseLength);
    }

    /**
     * 숫자 형식의 id를 가진 GET 요청이 왔을 때 id와 같은 task를 찾아 리턴한다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @param path 수신된 경로
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    private void sendDetailGetResponse(HttpExchange exchange, String path) throws IOException {
        String[] splitedPath = path.split("/");
        Optional<Task> storedTask = taskService.getTask(extractId(splitedPath));

        if (storedTask.isPresent()) {
            String content = taskMapper.taskToJson(storedTask.get());
            sendResponse(exchange, 200, content.getBytes().length);
            writeResponseBody(exchange, content);
            return;
        }

        sendResponse(exchange, 404, -1);
    }

    /**
     * GET 요청을 받아 저장된 Tasks를 찾아 리턴한다.
     *
     * @param exchange 수신된 요청과 응답을 설정할 수 있는 파라미터
     * @throws IOException 입출력에 문제가 발생하면 던집니다.
     */
    private void sendGetResponse(HttpExchange exchange) throws IOException {
        String content = taskMapper.tasksToJson(taskService.getTasks());
        sendResponse(exchange, 200, content.getBytes().length);
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
            sendResponse(exchange, 400, -1);
            return;
        }

        HashMap requestMap = taskMapper.getRequestMap(request);
        request = (String) requestMap.get("title");

        String content =  taskMapper.taskToJson(taskService.createTask(request));
        sendResponse(exchange, 201, content.getBytes().length);
        writeResponseBody(exchange, content);
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
}
