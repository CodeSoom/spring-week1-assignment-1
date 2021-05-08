package com.codesoom.assignment;

import com.codesoom.assignment.httpHandlers.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private int port = 8000;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        System.out.println(new App().getGreeting());

        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);
        try {
            HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
            HttpHandler handler = new TaskHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
