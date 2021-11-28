package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static final int PORT = 8000;
    public static final int BAGLOG = 0;

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, BAGLOG);

            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/", handler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
