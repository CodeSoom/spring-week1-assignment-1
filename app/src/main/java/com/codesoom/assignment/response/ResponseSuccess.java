package com.codesoom.assignment.response;

import com.codesoom.assignment.vo.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseSuccess {

    private final HttpExchange exchange;

    public ResponseSuccess(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public void send(String content, int httpStatus) throws IOException {
        exchange.sendResponseHeaders(httpStatus, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.close();
    }

    public void send(String content) throws IOException {
        send(content, HttpStatus.OK.getCode());
    }
}
