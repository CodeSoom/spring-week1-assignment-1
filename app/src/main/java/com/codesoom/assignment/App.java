package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static final int SERVER_PORT = 8000;
    public String getGreeting() {
        return "[Server Start ...]";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
            HttpServer httpServer = HttpServer.create(address, 0);

            HttpHandler handler = new TodoHttpHandler();
            httpServer.createContext("/", handler);

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
