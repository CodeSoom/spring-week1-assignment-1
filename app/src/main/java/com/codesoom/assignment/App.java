package com.codesoom.assignment;

import com.codesoom.assignment.handler.ServerHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress("localhost", 8000);
        HttpServer server = HttpServer.create(address, 0);


        HttpHandler handler = new ServerHttpHandler();


        server.createContext("/", handler);


        server.start();

    }
}
