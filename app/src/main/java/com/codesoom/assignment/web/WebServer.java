package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
    HttpServer httpServer;
    public WebServer(int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/");
    }

    public void start() {
        httpServer.start();
    }

    public void close() {
        httpServer.stop(0);
    }
}
