package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final int PORT = 8000;
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, 0);

            DemoHttpHandler httpHandler = new DemoHttpHandler();
            httpServer.createContext("/", httpHandler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
