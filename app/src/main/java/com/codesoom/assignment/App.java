package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final Integer PORT = 8000;
    private static final Integer BACKLOG = 0;

    public String getGreeting() {
        return "App Start!!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, BACKLOG);

            HttpHandler todoHttpHandler = new TodoHttpHandler();
            httpServer.createContext("/", todoHttpHandler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
