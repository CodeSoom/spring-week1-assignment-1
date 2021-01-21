package com.codesoom.assignment;

import com.codesoom.assignment.SpringHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    private static InetSocketAddress address = new InetSocketAddress(8000);
    public String getGreeting() {
        return "매일 매일 달리지기 위한 첫걸음 시작하기!";
    }


    public static void main(String[] args) {

        System.out.println(new App().getGreeting());

        try {
            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new SpringHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

