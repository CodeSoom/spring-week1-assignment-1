package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        final int PORT = 8000;
        final int BACK_LOG = 0;
        final List<Task> tasks = new LinkedList<>();

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
