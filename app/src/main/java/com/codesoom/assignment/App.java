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

        System.out.println(new App().getGreeting());

        try {
            int portNum = 8000;
            int backlogSettingNum =0;

            InetSocketAddress address =new InetSocketAddress(( portNum));
            HttpServer httpServer = HttpServer.create(address,backlogSettingNum);
            HttpHandler handler = new DemoHttpHandler();
            httpServer.createContext("/",handler);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}