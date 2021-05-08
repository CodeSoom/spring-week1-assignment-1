package com.codesoom.assignment;

import com.codesoom.assignment.httpHandlers.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    final private static int PORT = 8000;
    final private static String ROOT_PATH = "/";

    public static void main(String[] args) {
        System.out.println(">> API server start!");

        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        try {
            HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
            HttpHandler handler = new TaskHttpHandler();
            httpServer.createContext(ROOT_PATH, handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
