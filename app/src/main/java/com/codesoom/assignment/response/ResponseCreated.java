package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseCreated extends Response{
    private static final int Created = 201;

    public ResponseCreated(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int getHttpStatusCode() {
        return Created;
    }
}

