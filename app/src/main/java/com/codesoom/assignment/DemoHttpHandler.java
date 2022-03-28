package com.codesoom.assignment;

import com.codesoom.assignment.task.TaskList;
import com.codesoom.assignment.task.TaskRequest;
import com.codesoom.assignment.task.TaskService;
import com.codesoom.assignment.http.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static com.codesoom.assignment.task.TaskRequestType.*;
import static com.codesoom.assignment.http.HttpStatusCode.*;

public class DemoHttpHandler implements HttpHandler {

    private final TaskService taskService;

    public DemoHttpHandler() {
        TaskList taskList = new TaskList();
        taskService = new TaskService(taskList);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            TaskRequest taskRequest = new TaskRequest(exchange);
            processTask(taskRequest, exchange);
        } catch (IllegalArgumentException e) {
            response(exchange, BAD_REQUEST);
        } catch (IllegalStateException e) {
            response(exchange, NOT_FOUND);
        }
    }

    private void processTask(final TaskRequest taskRequest, final HttpExchange exchange) throws IOException {

        if (taskRequest.is(VIEW)) {
            String content = taskService.getTask(taskRequest.getId());
            response(exchange, OK, content);
            return;
        }

        if (taskRequest.is(SAVE)) {
            String requestBody = taskRequest.getRequestBody(exchange);
            String content = taskService.saveTask(requestBody);
            response(exchange, CREATED, content);
            return;
        }

        if (taskRequest.is(REPLACE)) {
            String requestBody = taskRequest.getRequestBody(exchange);
            String content = taskService.replaceTask(taskRequest.getId(), requestBody);
            response(exchange, OK, content);
            return;
        }

        if (taskRequest.is(MODIFY)) {
            String requestBody = taskRequest.getRequestBody(exchange);
            String content = taskService.modifyTask(taskRequest.getId(), requestBody);
            response(exchange, OK, content);
        }

        if (taskRequest.is(DELETE)) {
            taskService.deleteTask(taskRequest.getId());
            response(exchange, NO_CONTENT);
            return;
        }

        String content = taskService.getTasks();
        response(exchange, OK, content);
    }

    private void response(final HttpExchange exchange, final HttpStatusCode statusCode) throws IOException {

        long responseLength = statusCode.equals(NO_CONTENT) ? -1 : 0;

        exchange.sendResponseHeaders(statusCode.getCode(), responseLength);

        OutputStream responseOutputStream = exchange.getResponseBody();
        responseOutputStream.close();
    }

    private void response(final HttpExchange exchange,
                          final HttpStatusCode statusCode,
                          final String content) throws IOException {

        exchange.sendResponseHeaders(statusCode.getCode(), content.getBytes().length);

        OutputStream responseOutputStream = exchange.getResponseBody();
        responseOutputStream.write(content.getBytes());
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
