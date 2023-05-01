package com.codesoom.assignment;

import com.codesoom.assignment.httphandler.DemoHttpHandler;
import com.codesoom.assignment.httphandler.TaskHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private final static String HOST = "localhost";
    private final static int PORT = 8080;

    public static String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        try {
            InetSocketAddress address = new InetSocketAddress(HOST, PORT);
            HttpServer httpServer = HttpServer.create(address, 0);

            httpServer.createContext("/", new DemoHttpHandler());
            httpServer.createContext("/task", new TaskHttpHandler());
            httpServer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
