package com.codesoom.assignment;

import com.codesoom.assignment.handler.BaseHandler;
import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        final int PORT_NUMBER = 8000;
        final InetSocketAddress address = new InetSocketAddress(PORT_NUMBER);

        System.out.println("Codesoom HTTP server started on :" + address.getPort());

        try {
            HttpServer httpServer = HttpServer.create(address, 0);

            httpServer.createContext("/", new BaseHandler());
            httpServer.createContext("/tasks", new TaskHandler());

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
