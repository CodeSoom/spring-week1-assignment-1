package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int port = 8000;

    public static void main(String[] args) {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
            httpServer.createContext("/");
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
