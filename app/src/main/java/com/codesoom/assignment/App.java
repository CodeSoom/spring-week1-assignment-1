package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final int PORT = 8000;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(PORT);
        HttpServer httpServer = HttpServer.create(address, 0);

        HttpHandler TaskHttpHandler = new TaskHttpHandler();
        httpServer.createContext("/", TaskHttpHandler);

        httpServer.start();
    }
}
