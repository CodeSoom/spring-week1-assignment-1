package com.codesoom.assignment.server;

import com.codesoom.assignment.config.AppConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TodoServer {
    private final HttpServer httpServer;

    public TodoServer(AppConfig appConfig) throws IOException {
        InetSocketAddress address = new InetSocketAddress(appConfig.getHost(), appConfig.getPort());
        httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/", new TodoServerHandler(appConfig));
    }

    public void start() {
        httpServer.start();
    }

    public void close() {
        httpServer.stop(0);
    }

}
