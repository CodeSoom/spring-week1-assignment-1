package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class TaskHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String method = httpExchange.getRequestMethod();

        System.out.println(method + " " + path);

        String content = "Hello, World!";
        httpExchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }
}
