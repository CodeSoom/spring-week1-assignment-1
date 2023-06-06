package com.codesoom.assignment.task.handler.response;

import com.codesoom.assignment.common.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private final HttpExchange httpExchange;

    public HttpResponse(final HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void send(final HttpStatus httpStatus) throws IOException {
        httpExchange.sendResponseHeaders(httpStatus.getCode(), httpStatus.getContent().getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(httpStatus.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
