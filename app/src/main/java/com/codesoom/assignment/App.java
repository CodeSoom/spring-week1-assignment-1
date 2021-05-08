package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int PORT = 8000;
    private static final String CONTEXT_PAHT = "/";

    public String getGreeting() {
        return "Hello, HTTP Server...";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext(CONTEXT_PAHT, handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
