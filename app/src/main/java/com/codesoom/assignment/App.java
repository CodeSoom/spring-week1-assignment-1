package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException{
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        HttpServer httpServer = HttpServer.create(address,0);
        HttpHandler handler = new TodoHandler();

        httpServer.createContext("/", handler); // 주어진 경로로 들어오는 요청을 handler에서 처리하겠다.
        httpServer.start();
    }
}
