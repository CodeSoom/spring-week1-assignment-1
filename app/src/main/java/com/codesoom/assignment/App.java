package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final int DEFAULT_PORT = 8000;
    private static String ROOT_PATH = "/";
    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(DEFAULT_PORT);
            HttpServer httpServer = HttpServer.create(address,0);
            HttpHandler handler = new TaskHttpHandler();
            httpServer.createContext(ROOT_PATH,handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
