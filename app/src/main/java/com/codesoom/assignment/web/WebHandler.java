package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().equals("/tasks")) {
            String content = "[]";
            setJsonToResponseBody(exchange, content, 200);
        }
        else if (exchange.getRequestURI().getPath().equals("/task")){
            String content = "{\"id\":1,\"title\":\"Play Game\"}";
            setJsonToResponseBody(exchange, content, 201);
        }
        else {
            exchange.sendResponseHeaders(200, 0);
        }
    }
    private void setJsonToResponseBody(HttpExchange exchange, String content, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
