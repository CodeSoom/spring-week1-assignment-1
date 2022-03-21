package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static int PORT = 8000;

    public static void main(String[] args) {
        try {

            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address, 0);
            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
