package com.codesoom.assignment.response;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

public class ResponseNotFound extends Response{

    public ResponseNotFound(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected HttpStatusCode httpStatusCode() {
        return HttpStatusCode.NOT_FOUND;
    }
}
