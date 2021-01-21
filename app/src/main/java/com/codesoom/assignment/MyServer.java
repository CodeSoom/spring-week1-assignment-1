package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyServer {
    private static final int PORT = 8000;
    private InetSocketAddress address;
    private HttpServer httpServer;

    public MyServer() {
        this.address = new InetSocketAddress(PORT);
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(address, 0);
        HttpHandler handler = new MyHttpHandler();
        httpServer.createContext("/", handler);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
