package com.codesoom.assignment;

import com.codesoom.assignment.models.*;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class TasksHttpHandler implements HttpHandler {

    private TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().equals(TasksHttpRequest.TASKS)) {
            sendResponse(exchange, new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED));
            return;
        }

        HttpRequest httpRequest = new TasksHttpRequest(exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());

        if (!httpRequest.isValidMethod()) {
            sendResponse(exchange, new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED));
            return;
        }

        if (!httpRequest.isValidPath()) {
            sendResponse(exchange, new HttpResponse(HttpStatus.NOT_FOUND));
            return;
        }

        HttpRequestProcessor httpRequestProcessor = httpRequest.getMethod();
        HttpResponse response = httpRequestProcessor.processTasks(httpRequest, taskService);
        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
