package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseCreated extends HttpResponse {

    public HttpResponseCreated(HttpExchange exchange, String content) {
        super(exchange, content);
    }

    protected int httpStatusCode() {
        return 201;
    }
}
