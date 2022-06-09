package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

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

    public void sendResponseHeader(HttpExchange exchange) throws IOException {
       exchange.sendResponseHeaders(statusCode, message.getBytes().length);
    }
}
