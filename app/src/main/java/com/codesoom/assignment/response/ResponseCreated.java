package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseCreated extends Response {

    public ResponseCreated(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatus() {
        return 201;
    }
}
