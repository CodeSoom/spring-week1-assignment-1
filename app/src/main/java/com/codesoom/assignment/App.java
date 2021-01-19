package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * 1. Create HttpServer
 */



public class App {

    public static void main(String[] args) {
        try {
            InetSocketAddress address = new InetSocketAddress(8000);
            HttpServer server = HttpServer.create(address, 0);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
