package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseCreated extends HttpResponse{

    public HttpResponseCreated(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatusCode() {
        return 201;
    }
}
