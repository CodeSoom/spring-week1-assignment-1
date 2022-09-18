package com.codesoom.assignment;

import com.codesoom.assignment.model.Path;
import com.codesoom.assignment.model.ResponseData;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.service.TodoService;
import com.codesoom.assignment.service.TodoGetService;
import com.codesoom.assignment.service.TodoPostService;
import com.codesoom.assignment.service.TodoPutService;
import com.codesoom.assignment.service.TodoDeleteService;
import com.codesoom.assignment.util.HttpStatus;
import com.codesoom.assignment.util.HttpMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private final Map<HttpMethod, TodoService> methodMappingMap = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TodoHttpHandler() {
        initMethodMapping();
    }

    private void initMethodMapping() {
        methodMappingMap.put(HttpMethod.GET, new TodoGetService());
        methodMappingMap.put(HttpMethod.POST, new TodoPostService());
        methodMappingMap.put(HttpMethod.PUT, new TodoPutService());
        methodMappingMap.put(HttpMethod.PATCH, new TodoPutService());
        methodMappingMap.put(HttpMethod.DELETE, new TodoDeleteService());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();

        final String path = uri.getPath();
        final String requestBody = getRequestBody(exchange);
        String content = "";
        ResponseData responseData = null;

        if (isIncorrectURL(path)) {
            responseData = new ResponseData(HttpStatus.HTTP_NOT_FOUND, content);
        } else {
            Task task = null;
            String pathVariable = Path.getPathVariable(path);

            if (isExistRequestBody(requestBody)) {
                task = Task.valueOf(requestBody);
            }

            TodoService service = getService(method);
            if(service != null) {
                responseData = service.processRequest(exchange, task, pathVariable);
                content = responseData.getContent();
            } else {
                responseData = new ResponseData(HttpStatus.HTTP_BAD_METHOD, "");
            }
        }

        exchange.sendResponseHeaders(responseData.getStatusCode(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private TodoService getService(String method) {
        HttpMethod httpMethod = Arrays.stream(HttpMethod.values())
                .filter(m -> method.equals(m.name()))
                .findFirst()
                .orElse(null);

        return methodMappingMap.get(httpMethod);
    }

    private String getRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();

        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * URI 경로 path가 문자열 Path.REQUEST_MAPPING_URL 상수값을 포함하지 않는다면 true 리턴
     */
    private boolean isIncorrectURL(String path) {
        return !path.contains(Path.REQUEST_MAPPING_URL);
    }

    private boolean isExistRequestBody(String requestBody) {
        return !requestBody.isBlank();
    }
}
