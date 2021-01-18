package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(8000);

        HttpServer httpServer = null;
        try {
            httpServer = HttpServer.create(address, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpServer.start();
    }

}
