package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpStatus;
import com.codesoom.assignment.http.RequestMethod;
import com.codesoom.assignment.http.RequestUtils;
import com.codesoom.assignment.http.ResponseUtils;
import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskHttpHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Task> tasks = new ArrayList<>();

    private final Long ID_NOT_GIVEN = -1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String requestURI = uri.getPath();
        String requestBody = RequestUtils.readRequestBody(exchange.getRequestBody());
        RequestMethod requestMethod = RequestMethod.valueOf(exchange.getRequestMethod());

        if (!RequestUtils.isValidRequest(requestMethod, requestURI, requestBody)) {
            ResponseUtils.sendError(exchange, HttpStatus.BAD_REQUEST);
        }

        Optional<Long> optionalId = RequestUtils.getResourceId(requestURI);
        Long id = optionalId.orElse(ID_NOT_GIVEN);
        switch (requestMethod) {
            case GET:
                listTodos(exchange, id);
                break;
            case POST:
                createTodo(exchange, id, requestBody);
                break;
            case PUT:
            case PATCH:
                updateTodo(exchange, id, requestBody);
                break;
            case DELETE:
                deleteTodo(exchange, id);
                break;
            default:
        }
    }

    private void listTodos(HttpExchange exchange, Long id) throws IOException {
        try {
            String json = toJSON(id);
            ResponseUtils.sendResponse(exchange, json, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ResponseUtils.sendError(exchange, HttpStatus.NOT_FOUND);
        }
    }

    private void createTodo(HttpExchange exchange, Long id, String requestBody) throws IOException {

    }

    private void updateTodo(HttpExchange exchange, Long id, String requestBody) {

    }

    private void deleteTodo(HttpExchange exchange, Long id) {

    }

    /**
     * 요청 바디 데이터를 Task 객체로 변환합니다.
     * @param requestBody 요청 바디
     * @return Task 객체
     * @throws JsonProcessingException
     */
    private Task toTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    /**
     * 아이디를 기준으로 Task를 찾아 JSON으로 변환하여 반환합니다.
     * @param id Task 아이디
     * @return JSON 문자열
     * @throws IOException
     */
    private String toJSON(Long id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        if (id == ID_NOT_GIVEN) {
            objectMapper.writeValue(outputStream, tasks);
        } else {
            Task task = getTaskById(id);
            objectMapper.writeValue(outputStream, task);
        }
        return outputStream.toString();
    }

    /**
     * Task 아이디를 기준으로 목록에서 찾아 반환합니다.
     * @param id
     * @return Task 객체
     */
    private Task getTaskById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
