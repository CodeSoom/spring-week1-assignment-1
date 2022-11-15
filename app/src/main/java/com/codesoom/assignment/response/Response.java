package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Response {

    private final HttpExchange exchange;

    Response(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void sendResponse(String content) throws IOException {
        exchange.sendResponseHeaders(httpStatus(), content.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(content.getBytes());
        os.flush();
        os.close();
    }

    protected abstract int httpStatus();

}
