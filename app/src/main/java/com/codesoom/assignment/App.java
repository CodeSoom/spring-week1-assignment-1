package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            HttpServer httpServer = HttpServer.create(address, 0);

            HttpHandler handler = new TaskController();

            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
