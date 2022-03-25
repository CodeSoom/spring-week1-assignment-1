package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class TaskHandler extends CommonHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if(Objects.equals(method, "GET")){
            handleGetMethod(exchange);
        }else if(Objects.equals(method, "POST")){
            handlePostMethod(exchange);
        }else if(Objects.equals(method, "PUT") || Objects.equals(method, "PATCH")){
            handlePutOrPatchMethod(exchange);
        }else if(Objects.equals(method, "DELETE")){
            handleDeleteMethod(exchange);
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        String content = "Task: GET";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePostMethod(HttpExchange exchange) throws IOException {
        String content = "Task: POST";
        exchange.sendResponseHeaders(201, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePutOrPatchMethod(HttpExchange exchange) throws IOException {
        String content = "Task: PUT or PATCH";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handleDeleteMethod(HttpExchange exchange) throws IOException {
        String content = "Task: DELETE";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }
}
