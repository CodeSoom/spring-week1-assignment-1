package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static final int DEFAULT_PORT = 8000;
    public static final int DEFAULT_BACKLOG = 0;
    public static final String ROOT_CONTEXT = "/";

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), DEFAULT_BACKLOG);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext(ROOT_CONTEXT, handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
