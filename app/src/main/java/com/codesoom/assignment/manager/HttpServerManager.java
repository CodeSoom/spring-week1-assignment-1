package com.codesoom.assignment.manager;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {

    private HttpServer httpServer;

    public HttpServerManager(InetSocketAddress inetSocketAddress, int port) throws IOException {
        httpServer = HttpServer.create(inetSocketAddress, port);
    }

    public void setHandler(String path, HttpHandler handler) {
        httpServer.createContext(path, handler);
    }

    public void start() {
        httpServer.start();
    }
}
