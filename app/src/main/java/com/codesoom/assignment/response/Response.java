package com.codesoom.assignment.response;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Response {
    private final HttpExchange httpExchange;

    Response(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void send(String content) throws IOException {
        httpExchange.sendResponseHeaders(httpStatusCode().value(), content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    protected abstract HttpStatusCode httpStatusCode();
}
