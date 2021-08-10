package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseNotFound extends HttpResponse{

    public HttpResponseNotFound(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatusCode() {
        return 404;
    }
}
