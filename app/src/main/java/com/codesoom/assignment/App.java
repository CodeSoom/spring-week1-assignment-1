package com.codesoom.assignment;

import com.codesoom.assignment.handler.RequestHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(SERVER_HOST, SERVER_PORT);
        httpServer.bind(inetSocketAddress, 0);
        httpServer.createContext("/", exchange -> new RequestHandler().handle(exchange));
        httpServer.start();
    }
}
