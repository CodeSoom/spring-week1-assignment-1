package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseNotFound extends Response{

    public ResponseNotFound(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatus() {
        return 404;
    }
}
