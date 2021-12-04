package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Response {
    private final HttpExchange httpExchange;

    Response(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void send(String content) throws IOException {
        httpExchange.sendResponseHeaders(httpStatusCode(), content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    protected abstract int httpStatusCode();
}
