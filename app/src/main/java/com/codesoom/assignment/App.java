package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) {
        final int PORT = 8000;
        final int BACK_LOG = 0;

        try {
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, BACK_LOG);
            TaskRepository repository = new TaskRepository();
            TaskMapper mapper = new TaskMapper();
            HttpHandler handler = new DemoHttpHandler(repository, mapper);

            WebServer server = new WebServer(httpServer, handler);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
