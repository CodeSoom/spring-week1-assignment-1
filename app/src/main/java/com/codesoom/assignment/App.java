package com.codesoom.assignment;

import com.codesoom.assignment.http.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(PORT);

        HttpServer httpServer = HttpServer.create(address,0);
        HttpHandler handler = new TaskHttpHandler();

        httpServer.createContext("/", handler);
        httpServer.start();
    }
}
