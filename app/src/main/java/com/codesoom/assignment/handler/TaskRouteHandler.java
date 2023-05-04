package com.codesoom.assignment.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface TaskRouteHandler {

    boolean isSelect(String method, String path);

    void execute(HttpExchange exchange) throws IOException;

}
