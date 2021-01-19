package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handler is working");

        String content = "나는 진정 행복한 부자가 될 것이다.";

        exchange.sendResponseHeaders(200, content.getBytes().length);

        String method = exchange.getRequestMethod();
        System.out.println("Handler just requested a " + method + " method");

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();

    }
}
