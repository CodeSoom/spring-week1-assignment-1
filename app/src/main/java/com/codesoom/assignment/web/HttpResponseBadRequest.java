package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseBadRequest extends HttpResponse {

    public HttpResponseBadRequest(HttpExchange httpExchange) {
        super(httpExchange);
    }

    @Override
    protected int httpStatusCode() {
        return 400;
    }
}
