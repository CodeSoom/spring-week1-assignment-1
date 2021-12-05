package com.codesoom.assignment.response;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

public class ResponseBadRequest extends Response{

    public ResponseBadRequest(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected HttpStatusCode httpStatusCode() {
        return HttpStatusCode.BAD_REQUEST;
    }
}
