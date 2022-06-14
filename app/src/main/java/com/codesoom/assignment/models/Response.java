package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    HttpExchange exchange;
    private int statusCode;
    private String message;

    public Response(HttpExchange exchange) {
        this.exchange = exchange;
    }


    public void setSuccess(String message) {
        this.statusCode = HttpStatus.OK.value();
        this.message = message;
    }

    public void setCreated(String message) {
        this.statusCode = HttpStatus.CREATED.value();
        this.message = message;
    }

    public void setNotFound(String message) {
        this.statusCode = HttpStatus.NOT_FOUND.value();
        this.message = message;
    }

    public void setDeleteSuccess(String message) {
        this.statusCode = HttpStatus.DELETE_SUCCESS.value();
        this.message = message;
    }


    public void sendResponse() throws IOException {
        int messageLength = message.getBytes().length;
        if (message == "") {
            messageLength = -1;
        }
        exchange.sendResponseHeaders(statusCode,messageLength);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(message.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
