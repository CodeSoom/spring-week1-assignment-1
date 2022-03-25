package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(8000);
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new AppHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
