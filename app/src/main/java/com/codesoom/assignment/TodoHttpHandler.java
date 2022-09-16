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
import com.codesoom.assignment.util.RequestMethod;
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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private final Map<RequestMethod, TodoService> methodMappingMap = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TodoHttpHandler() {
        initMethodMapping();
    }

    private void initMethodMapping() {
        methodMappingMap.put(RequestMethod.GET, new TodoGetService());
        methodMappingMap.put(RequestMethod.POST, new TodoPostService());
        methodMappingMap.put(RequestMethod.PUT, new TodoPutService());
        methodMappingMap.put(RequestMethod.PATCH, new TodoPutService());
        methodMappingMap.put(RequestMethod.DELETE, new TodoDeleteService());
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
                task = convertToTask(requestBody);
            }

            TodoService service = getService(method);
            responseData = service.processRequest(exchange, task, pathVariable);
            content = responseData.getContent();
        }

        exchange.sendResponseHeaders(responseData.getStatusCode(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private TodoService getService(String method) {
        return methodMappingMap.get(RequestMethod.valueOf(method));
    }

    private String getRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();

        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    /**
     * 요청 URI가 /tasks가 맞는지 여부 확인
     * ex)
     * /tasks/1 (O)
     * /abcd/3 (X)
     * @param path URI 경로
     */
    private boolean isIncorrectURL(String path) {
        return !path.contains(Path.REQUEST_MAPPING_URL);
    }

    private boolean isExistRequestBody(String requestBody) {
        return !requestBody.isBlank();
    }

    private Task convertToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);

    }
}
