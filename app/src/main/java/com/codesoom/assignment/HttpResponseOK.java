package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

public class HttpResponseOK extends HttpResponse{

    public HttpResponseOK(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    protected int httpStatusCode() {
        return 200;
    }
}
