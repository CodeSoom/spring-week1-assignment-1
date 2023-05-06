package com.codesoom.assignment.task.handler.response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private final HttpExchange httpExchange;

    public HttpResponse(final HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void send(final int statusCode,
                     final String content) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
