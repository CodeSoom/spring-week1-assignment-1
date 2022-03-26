package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Objects;

public class AppHandler extends CommonHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if(path.startsWith("/tasks/")){
            TaskByIdHandler taskByIdHandler = new TaskByIdHandler();
            taskByIdHandler.handle(exchange);
        }else if(Objects.equals(path, "/tasks")){
            TaskHandler taskHandler = new TaskHandler();
            taskHandler.handle(exchange);
        }else if(Objects.equals(path, "/")){
            String content = "This app is working.";
            exchange.sendResponseHeaders(200, content.getBytes().length);
            outputResponse(exchange, content);
        }
    }
}
