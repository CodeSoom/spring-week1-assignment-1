package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) {
        connectHttpServer(
                new InetSocketAddress(8000),
                new TaskHttpHandler());
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
