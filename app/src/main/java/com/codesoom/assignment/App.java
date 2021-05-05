package com.codesoom.assignment;

import com.codesoom.assignment.handler.BaseHandler;
import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        System.out.println("Running Codesoom server...");

        try {
            InetSocketAddress address = new InetSocketAddress(8000);
            HttpServer httpServer = HttpServer.create(address, 0);

            httpServer.createContext("/", new BaseHandler());
            httpServer.createContext("/tasks", new TaskHandler());

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
