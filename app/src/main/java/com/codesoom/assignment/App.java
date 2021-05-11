package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static int port = 8000;
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(port);
        HttpServer httpServer = HttpServer.create(address, 0);

        HttpHandler handler = new TodoRestApiHandler();
        httpServer.createContext("/", handler);

        httpServer.start();
    }
}
