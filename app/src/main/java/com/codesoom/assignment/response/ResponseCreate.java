package com.codesoom.assignment.response;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

public class ResponseCreate extends Response{

    public ResponseCreate(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected HttpStatusCode httpStatusCode() {
        return HttpStatusCode.CREATED;
    }
}
