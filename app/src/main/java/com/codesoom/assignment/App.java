package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int serverPort = 8899;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(serverPort);
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/" , handler);
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
