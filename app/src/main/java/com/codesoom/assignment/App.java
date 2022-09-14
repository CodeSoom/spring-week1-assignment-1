package com.codesoom.assignment;

import com.codesoom.assignment.util.HttpConst;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "[Server Start ...]";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(HttpConst.SERVER_PORT);
            HttpServer httpServer = HttpServer.create(address, 0);

            HttpHandler handler = new TodoHttpHandler();
            httpServer.createContext("/", handler);

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
