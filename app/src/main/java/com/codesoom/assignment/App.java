package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    private final static int PORT = 8000;

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(PORT);
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
            System.out.println("localhost:" + PORT + "/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
