package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseOK extends HttpResponse {

    public HttpResponseOK(HttpExchange exchange, String content) {
        super(exchange, content);
    }

    @Override
    protected int httpStatusCode() {
        return 200;
    }
}
