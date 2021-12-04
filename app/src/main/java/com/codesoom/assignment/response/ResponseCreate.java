package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseCreate extends Response{

    public ResponseCreate(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected int httpStatusCode() {
        return 201;
    }
}
