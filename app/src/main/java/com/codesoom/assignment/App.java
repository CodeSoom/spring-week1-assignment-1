package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {

        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8000);

        try {
            HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
            HttpHandler httpHandler = new DemoHttpHandler();
            httpServer.createContext("/", httpHandler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
