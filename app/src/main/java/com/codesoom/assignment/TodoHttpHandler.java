package com.codesoom.assignment;

import com.codesoom.assignment.models.TasksPath;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HTTP 핸들러
 */
public class TodoHttpHandler implements HttpHandler {
    private final Map<Long, Task> taskMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private long lastTaskId = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TasksPath path = new TasksPath(exchange.getRequestURI().getPath());
        if (path.isValidUrl()) {
            Response response = processRequest(exchange);
            response.sendResponse(exchange);
            return;
        }
        Response response = new ResponseNoContent("");
        response.sendResponse(exchange);
    }

    /**
     * 들어온 http 요청에 맞는 작업을 호출합니다.
     *
     * @param exchange
     * @return  Response
     * @throws IOException
     */
    private Response processRequest(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                return processGetRequest(exchange);
            case "POST":
                return processPostRequest(exchange);
            case "PUT":
            case "PATCH":
                return processPutAndPatchRequest(exchange);
            case "DELETE":
                return processDeleteRequest(exchange);
            case "HEAD":
                return new ResponseSuccess("");
            default:
                return new ResponseNotFound("");
        }
    }

    /**
     * Get요청시 파마리터가 있는지 확인하고 그에 맞는 응답을 반환합니다.
     *
     * @param exchange
     * @return Response
     * @throws IOException
     */
    private Response processGetRequest(HttpExchange exchange) throws IOException {
        TasksPath path = new TasksPath(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseSuccess(taskToJson());
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        return new ResponseSuccess(taskToJson(inputId));
    }

    private Response processPostRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        Task task = jsonToTask(body);
        taskMap.put(lastTaskId, task);
        return new ResponseCreated(taskToJson(lastTaskId++));
    }

    private Response processPutAndPatchRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        TasksPath path = new TasksPath(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseNotFound("");
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        Task task = jsonToTask(body);
        taskMap.put(inputId, task);
        return new ResponseSuccess(taskToJson(inputId));
    }

    private Response processDeleteRequest(HttpExchange exchange) {
        TasksPath path = new TasksPath(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseNotFound("");
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        taskMap.remove(inputId);
        return new ResponseNoContent("");
    }

    private String getStringBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * Map에 해당 id가 있다면 true를 반환합니다.
     *
     * @param id
     * @return
     */
    private boolean hasId(long id) {
        return taskMap.containsKey(id);
    }

    /**
     *  JsonArray형태의 문자열을 Task 객체로 반환합니다.
     *
     * @param content
     * @return Task
     * @throws JsonProcessingException
     */
    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId(lastTaskId);
        return task;
    }

    /**
     * taskMap에 있는 모든 Task를 JsonArray 형태의 문자열로 반환합니다.
     *
     * @return JsonArray 형태의 String
     * @throws IOException
     */
    private String taskToJson() throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, new ArrayList<>(taskMap.values()));
        return outputstream.toString();
    }

    /**
     * taskMap에 있는 특정 id를 JsonArray 형태의 문자열로 반환합니다.
     *
     * @param id
     * @return JsonArray 형태의 String
     * @throws IOException
     */
    private String taskToJson(long id) throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, taskMap.get(id));
        return outputstream.toString();
    }
}
