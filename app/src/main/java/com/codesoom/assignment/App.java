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

        final int SOCKET_PORT = 8000;
        final int MAX_BACKLOG = 0;

        try {
            InetSocketAddress address = new InetSocketAddress(SOCKET_PORT);

            /*
             * HttpServer.create 0은 backlog로 서버 요청에 대기할 수 있는 최대 수를 지정할 수 있습니다.
             * 0을 넘기면 대기열 없이 누군가 서버에 요청이 들어와 있다면 다른사용자는 대기가 아니라 삭제가 될것 같습니다.
             */
            HttpServer server = HttpServer.create(address,MAX_BACKLOG);

            HttpHandler handler = new CrudHttpHandler();
            server.createContext("/",handler);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new App().getGreeting());
    }
}
