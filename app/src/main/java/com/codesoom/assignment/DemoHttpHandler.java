package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handler is working");

        exchange.sendResponseHeaders(200, 0);

        String method = exchange.getRequestMethod();
        System.out.println("Handler just requested a " + method + " method");

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.close();

        "pr이 자동으로 업데이트되는지 테스트합니다.";
    }
}
