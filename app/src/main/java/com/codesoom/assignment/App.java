package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress(8000);
        try {
            HttpServer httpServer = HttpServer.create(address, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
