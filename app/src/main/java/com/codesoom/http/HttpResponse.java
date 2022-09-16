package com.codesoom.http;

import com.codesoom.assignment.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// todo 클래스로 분리하긴 했는데 역할이 애매한 것 같다
public class HttpResponse {
    private final HttpExchange exchange;

    public HttpResponse(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void response(HttpStatus status, String content) throws IOException {
        exchange.sendResponseHeaders(status.getCode(), content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes(StandardCharsets.UTF_8));
        responseBody.flush();
        responseBody.close();
    }
}
