package com.codesoom.assignment;

import com.codesoom.assignment.handler.DemoHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    final static int SERVER_PORT = 8000;

    public static void main(String[] args) {

        System.out.println(">>>>>>>>>>>>>>>>>> Access CodeSoom Assignment RestAPI");

        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);

        try {

            HttpServer httpServer = HttpServer.create(address, 0);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
            System.out.println(">>>>>>>>>>>>>>>>>>  Server start success!! ( Port : " + SERVER_PORT + " )");

        } catch (IOException e) {

            System.err.println(">>>>>>>>>>>>>>>>>>  Server start fail!! ( Port : " + SERVER_PORT + " )");
            e.printStackTrace();

        }

    }

}
