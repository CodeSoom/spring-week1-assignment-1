package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseNoContent extends Response {
    private static final int NO_CONTENT = 204;

    public ResponseNoContent(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int getHttpStatusCode() {
        return NO_CONTENT;
    }
}
