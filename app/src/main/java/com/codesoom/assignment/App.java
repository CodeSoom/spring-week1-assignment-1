package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(Constant.PORT), Constant.BACKLOG);
            httpServer.createContext("/", new DemoHttpHandler());
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
