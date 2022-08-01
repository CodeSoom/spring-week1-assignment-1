package com.codesoom.assignment.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler implements HttpHandler {

    public final int SUCCESSFUL_OK = 200;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        System.out.println(String.format("[method] : %s , [path] : %s" , method , path));

        String content = "Hello Codesoom!!!";

        exchange.sendResponseHeaders(SUCCESSFUL_OK , content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
