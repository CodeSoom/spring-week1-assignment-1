package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello, this is TODO App server...";
    }

    private static final String HOST = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(HOST, PORT);
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler taskHttpHandler = new TaskHttpHandler();
            httpServer.createContext("/tasks", taskHttpHandler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
