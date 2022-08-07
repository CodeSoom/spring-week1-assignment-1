package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
        InetSocketAddress address = new InetSocketAddress(8000);

        try {
            HttpServer server = HttpServer.create(address , 0);
            HttpHandler handler = new TaskHandler();
            server.createContext("/" , handler);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
