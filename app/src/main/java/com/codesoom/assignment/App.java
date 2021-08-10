package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) {
        final InetSocketAddress address = new InetSocketAddress(PORT);
        final HttpHandler taskHandler = new TaskHandler();

        try {
            final HttpServer server = HttpServer.create(address, 0);
            server.createContext(TaskHandler.HANDLER_PATH, taskHandler);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
