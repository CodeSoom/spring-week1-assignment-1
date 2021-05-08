package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static final int PORT_NUMBER = 9090;

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress("localhost",PORT_NUMBER);
            HttpServer httpServer = HttpServer.create(address,0);

            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/",handler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
