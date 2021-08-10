package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public abstract class HttpResponse {

    private final HttpExchange exchange;

    public HttpResponse(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void send(String content) throws IOException {
        exchange.sendResponseHeaders(httpStatusCode(), content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    protected abstract int httpStatusCode();
}
