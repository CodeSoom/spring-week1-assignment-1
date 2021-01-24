package com.codesoom.assignment;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {

    private static int PORT_NUMBER = 8000;
    private static int BACKLOG = 0;
    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(PORT_NUMBER);
            HttpServer httpserver = HttpServer.create(address, BACKLOG);
            HttpHandler handler = new DemoHttpHandler();
            httpserver.createContext("/", handler);
            httpserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
