package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(8080);
        HttpHandler handler = new TaskHttpHandler();

        connectHttpServer(address, handler);
    }
    // HttpServer에 연결한다.
    private static void connectHttpServer(InetSocketAddress address, HttpHandler handler) {
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
