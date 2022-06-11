package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private int statusCode;
    private String message;


    public int getStatusCode() {
        return statusCode;
    }

    public void setResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void sendResponse(HttpExchange exchange) throws IOException {
       exchange.sendResponseHeaders(statusCode, message.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(message.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
