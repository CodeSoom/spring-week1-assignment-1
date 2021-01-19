package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpRequestMethod;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {

    private TaskService taskService = new TaskService();
    private static final String TASKS = "/tasks";
    private static final String TASKS_PATTERN = TASKS + "/*[0-9]*";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());

        if(!isValidRequest(httpRequest)) {
            sendResponse(exchange, createWrongRequestResponse());
            return;
        }

        sendResponse(exchange, getHttpResponse(httpRequest));
    }

    private HttpResponse getHttpResponse(HttpRequest httpRequest) {
        return switch (httpRequest.getMethod()) {
            case GET -> createGetResponse(httpRequest.getPath());
            case POST -> createPostResponse(httpRequest.getBody());
            case PATCH, PUT -> createPatchOrPutResponse(httpRequest.getPath(), httpRequest.getBody());
            case DELETE -> createDeleteResponse(httpRequest.getPath());
            default -> createWrongMethodResponse();
        };
    }

    private HttpResponse createGetResponse(String path) {
        if (path.equals(TASKS)) {
            return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_OK, taskService.getTasks());
        }

        Long id = getIdFromPath(path);
        String content = taskService.getTask(id);

        if (content.isEmpty()) {
            return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NOT_FOUND);
        }

        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_OK, content);
    }

    private HttpResponse createPostResponse(String body) {
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_CREATED, taskService.addTask(body));
    }

    private HttpResponse createPatchOrPutResponse(String path, String body) {
        Long id = getIdFromPath(path);

        if(taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NOT_FOUND);
        }

        String content = taskService.updateTask(id, body);
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_OK, content);
    }

    private HttpResponse createDeleteResponse(String path) {
        Long id = getIdFromPath(path);

        if(taskService.getTask(id).isEmpty()) {
            return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NOT_FOUND);
        }

        taskService.deleteTask(id);
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NO_CONTENT);
    }

    private HttpResponse createWrongMethodResponse() {
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_METHOD_NOT_ALLOWED);
    }

    private HttpResponse createWrongRequestResponse() {
        return new HttpResponse(HttpResponse.HTTP_STATUS_CODE_NOT_FOUND);
    }

    private void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private boolean isValidRequest(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        HttpRequestMethod method = httpRequest.getMethod();

        return switch (method) {
            case GET -> path.equals(TASKS) || path.matches(TASKS_PATTERN);
            case POST -> path.equals(TASKS);
            case PATCH, PUT, DELETE -> path.matches(TASKS_PATTERN);
            default -> false;
        };
    }

    private Long getIdFromPath(String path) {
        Long id = Long.valueOf(path.replace(TASKS + "/", ""));
        return id;
    }

}
