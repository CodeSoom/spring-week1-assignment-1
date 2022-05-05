package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        final int PORT = 8000;
        final int BACK_LOG = 0;
        final Map<Long, Task> tasks = new HashMap<>();

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, BACK_LOG);
            HttpHandler handler = new DemoHttpHandler(tasks);

            WebServer server = new WebServer(httpServer, handler);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
