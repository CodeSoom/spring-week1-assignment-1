package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class SpringHandler implements HttpHandler {
    static final int httpStatus = 200;
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println(method + " " + path);

        String content = "매일 매일 달리지기 위한 첫걸음 시작하기!";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = "[{\"id\":1, \"title\":\"Do nothing\"}]";
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }


        exchange.sendResponseHeaders(httpStatus, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
