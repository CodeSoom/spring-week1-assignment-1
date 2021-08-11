package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    private static final int PORT = 8000;
    private static final int BACK_LOG = 0;

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress(PORT);
        try {
            HttpServer httpServer = HttpServer.create(address, BACK_LOG);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
