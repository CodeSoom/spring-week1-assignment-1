package com.codesoom.assignment;

import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.service.*;
import com.codesoom.assignment.util.HttpConst;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    public static final String REQUEST_MAPPING_URL = "/tasks";
    private final Map<String, TodoService> methodMappingMap = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TodoHttpHandler() {
        initMethodMapping();
    }

    private void initMethodMapping() {
        methodMappingMap.put("GET", new TodoGetService());
        methodMappingMap.put("POST", new TodoPostService());
        methodMappingMap.put("PUT", new TodoPutService());
        methodMappingMap.put("DELETE", new TodoDeleteService());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();

        String path = uri.getPath();
        String requestBody = getRequestBody(exchange);
        String content = "";


        if(isIncorrectURL(path)){
            exchange.sendResponseHeaders(HttpConst.HTTP_NOT_FOUND, content.getBytes().length);
        } else {
            Task task = null;
            String pathVariable =  extractPathVariable(path);

            if (!requestBody.isBlank()) {
                task = convertToTask(requestBody);
            }

            TodoService service = getService(method);
            ResponseData responseData = service.processRequest(exchange, task, pathVariable);

            content = responseData.getContent();
            exchange.sendResponseHeaders(responseData.getStatusCode(), content.getBytes().length);
        }

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private TodoService getService(String method) {
        return methodMappingMap.get(method);
    }

    private String getRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();

        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private boolean isIncorrectURL(String path) {
        return !path.contains(REQUEST_MAPPING_URL);
    }

    private String extractPathVariable(String path) {
        String[] splitPaths = path.replace(REQUEST_MAPPING_URL, "").split("/");

        if(splitPaths.length == 1 && splitPaths[0].isBlank()) return null;
        if (splitPaths.length != 2) return "";
        if (!isNumeric(splitPaths[1])) return "";

        return splitPaths[1];
    }

    private boolean isNumeric(String value) {
        return value.matches("[+-]?\\d*(\\.\\d+)?");
    }

    private Task convertToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);

    }
}
