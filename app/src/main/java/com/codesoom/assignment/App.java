package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress("localhost", 8000);
        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create(address, 0);
            httpServer.start();
            CustomHttpHandler customHttpHandler = new CustomHttpHandler();
            httpServer.createContext("/", customHttpHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
