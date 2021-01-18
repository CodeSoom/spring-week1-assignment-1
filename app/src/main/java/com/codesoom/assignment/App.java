package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(addr, 0);
        httpServer.createContext("/",new TodoHttpHandler());
        httpServer.start();
    }
}
