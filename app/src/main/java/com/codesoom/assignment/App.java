package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {

        try {
            InetSocketAddress address = new InetSocketAddress(8000);
            HttpServer server = HttpServer.create(address,0);

            HttpHandler handler = new CrudHttpHandler();
            server.createContext("/",handler);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new App().getGreeting());
    }
}
