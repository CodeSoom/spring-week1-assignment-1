package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        MyHttpServer httpServer = new MyHttpServer(8000);
        HttpHandler handler = new MyHandler();
        httpServer.addHandler("/", handler);
        httpServer.start();
    }
}
