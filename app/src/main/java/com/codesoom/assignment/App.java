package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
            HttpHandler handler = new TaskHandler();
            httpServer.createContext("/tasks", handler);

            httpServer.start();
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }
}
