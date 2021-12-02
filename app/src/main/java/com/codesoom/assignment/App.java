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
            InetSocketAddress address = new InetSocketAddress(8000); //서버 정보
            HttpServer httpServer = HttpServer.create(address, 0);

            HttpHandler handler = new DemoHttpHandler(); //클라이언트 요청 처리하는 클래스
            httpServer.createContext("/", handler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
