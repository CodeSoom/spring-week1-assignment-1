package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello, this is TODO App server...";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress("localhost", 8000);
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new TaskHttpHandler();
            httpServer.createContext("/tasks", handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}