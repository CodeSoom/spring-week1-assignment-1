package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    static final int PORT = 8000;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new App().getGreeting());

        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
        HttpHandler handler = new DemoHttpHandler();
        httpServer.createContext("/", handler);
        httpServer.start();
    }
}
