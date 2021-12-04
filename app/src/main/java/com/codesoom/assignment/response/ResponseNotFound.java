package com.codesoom.assignment.response;

import com.sun.net.httpserver.HttpExchange;

public class ResponseNotFound extends Response{

    public ResponseNotFound(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected int httpStatusCode() {
        return 404;
    }
}
