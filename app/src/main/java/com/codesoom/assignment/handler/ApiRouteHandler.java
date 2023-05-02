package com.codesoom.assignment.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

@FunctionalInterface
public interface ApiRouteHandler {

    void handle(final HttpExchange exchange) throws IOException;

}
