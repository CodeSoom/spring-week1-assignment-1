package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseNoContent extends HttpResponse {

    public HttpResponseNoContent(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatusCode() {
        return 204;
    }
}
