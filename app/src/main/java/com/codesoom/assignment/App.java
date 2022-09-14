package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new TaskHttpHandler();
            httpServer.createContext("/", handler);

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
