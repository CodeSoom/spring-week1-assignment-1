package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) {
        final int PORT_NUM = 8000;

        try {
            InetSocketAddress address = new InetSocketAddress(PORT_NUM);
            HttpServer httpServer = HttpServer.create(address, 0);

            HttpHandler handler = new TodoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
