package com.codesoom.assignment.response;

import com.codesoom.assignment.enums.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;

public class ResponseNoContent extends Response {

    public ResponseNoContent(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected HttpStatusCode httpStatusCode() {
        return HttpStatusCode.NO_CONTENT;
    }


}
