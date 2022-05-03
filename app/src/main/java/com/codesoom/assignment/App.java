package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        int port = 8000;
        int backLog = 0;
        String path = "/";

        InetSocketAddress address = new InetSocketAddress(port);
        HttpServer httpServer = HttpServer.create(address, backLog);
        HttpHandler handler = new AssignmentHttpHandler();
        httpServer.createContext(path, handler);
        httpServer.start();
    }
}
