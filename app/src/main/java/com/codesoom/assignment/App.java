package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import demo.codesoom.com.DemoHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

@SpringBootApplication
public class App {
    public String getGreeting() {
        return "HTTP START";
    }

    public static void main(String[] args) {
//        final String LOCALHOST = "localhost";
//        final int PORT = 8000;
//        System.out.println(new App().getGreeting());
//        try {
//            InetSocketAddress address = new InetSocketAddress(LOCALHOST, PORT); // localhost는 생략해도 된다.
//            HttpServer httpServer = HttpServer.create(address, 0);
//            HttpHandler handler = new MyHttpHandler();
//            httpServer.createContext("/", handler);// path를 쓰게 돼있다 (아까 본 json 기입)
//            httpServer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(App.class, args); // 실행 시 첫번째 인자의 클래스가 들어간다.

    }
}
