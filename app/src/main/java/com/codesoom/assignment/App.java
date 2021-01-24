package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final int PORT = 8000;
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        final List<Task> tasks = new ArrayList<>();

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, 0);

            DemoHttpHandler httpHandler = new DemoHttpHandler(tasks);

            httpServer.createContext("/", httpHandler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
