package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {
    private final HttpServer server;
    private final HttpHandler handler;

    public WebServer(HttpServer server, HttpHandler handler) {
        this.server = server;
        this.handler = handler;
    }

    public void run() {
        server.createContext("/", handler);
        server.start();
    }

}
