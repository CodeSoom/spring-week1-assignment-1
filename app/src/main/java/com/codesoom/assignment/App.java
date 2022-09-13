package com.codesoom.assignment;

import com.codesoom.assignment.handler.DemoHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(8000);

        HttpServer httpServer = HttpServer.create(address, 0);

        HttpHandler httpHandler = new DemoHttpHandler();

        httpServer.createContext("/", httpHandler);
        httpServer.start();
    }
}
