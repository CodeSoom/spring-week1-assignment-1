package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        final InetSocketAddress address = new InetSocketAddress(8000);

        final HttpServer httpServer;
        try {
            httpServer = HttpServer.create(address, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final HttpHandler handler = new ToDoHttpHandler();
        httpServer.createContext("/", handler);
        httpServer.start();
    }
}
