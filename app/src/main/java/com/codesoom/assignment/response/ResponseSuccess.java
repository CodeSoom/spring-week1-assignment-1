package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseSuccess extends Response{

    public ResponseSuccess(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected int httpStatusCode() {
        return 200;
    }
}
