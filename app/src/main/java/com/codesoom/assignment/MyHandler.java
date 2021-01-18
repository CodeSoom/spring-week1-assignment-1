package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class MyHandler implements HttpHandler {

    private TaskService taskService;

    public MyHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestInfo requestInfo = new RequestInfo(exchange);
        printRequestInfo(requestInfo);
    }

    private void printRequestInfo(RequestInfo requestInfo) {
        System.out.println("Received new request - " + requestInfo.toString());
    }
}
