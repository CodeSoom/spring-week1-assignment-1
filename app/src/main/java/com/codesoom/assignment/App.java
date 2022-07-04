package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(8000);

        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new ToDoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            // 이 상황에서는 에러 처리 어떻게 해주면 좋을까?
            throw new RuntimeException(e);
        }
    }
}
