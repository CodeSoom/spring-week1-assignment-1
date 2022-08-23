package com.codesoom.assignment;

import com.codesoom.assignment.impl.AssignmentHttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static final IHttphandler httphandler = new AssignmentHttpHandler();

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/", httphandler);
        httpServer.start();
    }
}
