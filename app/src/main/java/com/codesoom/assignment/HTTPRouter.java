package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

public class HTTPRouter {

    private HttpExchange exchange;

    public HTTPRouter(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public HTTPMethod getHttpMethod() {
        final String method = exchange.getRequestMethod();

        try {
            return HTTPMethod.valueOf(method);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
