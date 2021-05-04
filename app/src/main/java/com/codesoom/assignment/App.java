package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {


        // 서버 생성 및 주소, 포트 설정
        InetSocketAddress address = new InetSocketAddress("localhost", 8000);
        HttpServer server = HttpServer.create(address, 0);

        // Handler 생성
        HttpHandler handler = new ServerHttpHandler();

        // Path 지정
        server.createContext("/", handler);

        // 웹 서버 구동
        server.start();

    }
}
