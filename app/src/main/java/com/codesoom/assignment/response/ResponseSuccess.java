package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseSuccess extends Response {

    public ResponseSuccess(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatus() {
        return 200; //OK
    }

}
