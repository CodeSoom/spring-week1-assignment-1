package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {

        final int PORT = 8000;

        try {
            InetSocketAddress serverAddress = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(serverAddress, 0);

            HttpHandler handler = new MyHttpHandler();

            httpServer.createContext("/", handler);

            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getGreeting() {
        return "Hello World!";
    }
}
