package com.codesoom.assignment;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {

    public static void main(String[] args) {

        try {
            InetSocketAddress address = new InetSocketAddress(8001);
            HttpServer httpserver = HttpServer.create(address, 0);
            HttpHandler handler = new DemoHttpHandler();
            httpserver.createContext("/", handler);
            httpserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
