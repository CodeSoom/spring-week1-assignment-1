package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The {@code MyHttpServer} class is HTTP Server
 */
public class MyHttpServer {
    private final HttpServer httpServer;

    public MyHttpServer(int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(port);
        httpServer = HttpServer.create(address, 0);
    }

    public void addHandler(String path, HttpHandler handler) {
        httpServer.createContext(path, handler);
        System.out.println("Added new handler at - " + path);
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP Server Started!");
    }

}
