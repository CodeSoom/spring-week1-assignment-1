package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    static final int PORT = 8000; //포트번호
    static final int BACKLOG = 0; //클라이언트가 요청이 오면 대기열에서 기다리는 갯수

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        try {
            InetSocketAddress address = new InetSocketAddress(PORT); //서버 정보
            HttpServer httpServer = HttpServer.create(address, BACKLOG);

            String path = "/"; //클라이언트의 요청을 받아들일 수 있는 최소한의 path
            HttpHandler handler = new DemoHttpHandler(); //클라이언트 요청 처리하는 클래스
            httpServer.createContext(path, handler);

            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
