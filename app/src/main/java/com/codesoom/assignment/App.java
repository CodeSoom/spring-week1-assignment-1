package com.codesoom.assignment;

import com.codesoom.assignment.handler.RequestHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8000);
        httpServer.bind(inetSocketAddress, 0);
        httpServer.createContext("/", exchange -> new RequestHandler().handle(exchange));
        httpServer.start();
    }
}
