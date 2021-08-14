package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseNotFound extends HttpResponse {

    public HttpResponseNotFound(HttpExchange exchange, String content) {
        super(exchange, content);
    }

    @Override
    protected int httpStatusCode() {
        return 404;
    }
}
