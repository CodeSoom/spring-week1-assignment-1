package com.codesoom.assignment.task.server;

import com.codesoom.assignment.task.handler.HttpRequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleHttpServer {

    private final static String LOCAL_HOST = "localhost";
    private final static int PORT = 8000;

    private final HttpRequestHandler httpRequestHandler;

    public SimpleHttpServer(final HttpRequestHandler httpRequestHandler) {
        this.httpRequestHandler = httpRequestHandler;
    }

    public void run() {
        try {
            InetSocketAddress address = new InetSocketAddress(LOCAL_HOST, PORT);
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.createContext("/", httpRequestHandler);
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
