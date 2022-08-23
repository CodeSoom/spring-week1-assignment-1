package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public interface IHttphandler extends HttpHandler {

    public void handle(HttpExchange exchange) throws IOException;
}
