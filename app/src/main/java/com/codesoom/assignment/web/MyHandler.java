package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MyHandler implements HttpHandler {

    private final TaskService taskService;
    private final HttpRequestContext requestContext;

    public MyHandler(TaskService taskService, HttpRequestContext requestContext) {
        this.taskService = taskService;
        this.requestContext = requestContext;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange);
        printHttpRequest(httpRequest);

        requestContext.processRequest(httpRequest, exchange, taskService);
    }

    private void printHttpRequest(HttpRequest httpRequest) {
        System.out.println("Received new request - " + httpRequest.toString());
    }

}
