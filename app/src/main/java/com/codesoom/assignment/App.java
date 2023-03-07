package com.codesoom.assignment;

import com.codesoom.assignment.handler.DemoHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class App {
    private static int PORT = 8000;
    public static void main(String[] args) {

        try{
            InetSocketAddress address = new InetSocketAddress(PORT);
            HttpServer httpServer = HttpServer.create(address , 0);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/", handler);
            httpServer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
