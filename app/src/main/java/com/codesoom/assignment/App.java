package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int PORT = 8000;
    private static final int ZERO = 0;
    private static final String ROOT_PATH = "/";

    public static void main(String[] args) {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(inetSocketAddress, ZERO);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext(ROOT_PATH, handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
