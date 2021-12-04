package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseBadRequest extends Response{

    public ResponseBadRequest(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected int httpStatusCode() {
        return 400;
    }
}
