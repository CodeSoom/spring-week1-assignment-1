package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final String HOSTNAME = "localhost";
    private static final int PORT_NUMBER = 8000;

    private static final int SOCKET_BACKLOG = 0;

    private static final String ROOT_PATH = "/";

    public static void main(String[] args) {

        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOSTNAME, PORT_NUMBER);

        try {
            HttpServer httpServer = HttpServer.create(inetSocketAddress, SOCKET_BACKLOG);
            HttpHandler httpHandler = new DemoHttpHandler();
            httpServer.createContext(ROOT_PATH, httpHandler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
