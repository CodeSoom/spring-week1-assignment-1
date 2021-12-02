package com.codesoom.assignment;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        // ip는 와일드카드 이고 port 가 8000 인 소켓 주소를 작성한다.
        InetSocketAddress address = new InetSocketAddress(8000);

        // 위 address 로 httpServer 를 생성한다.
        // backlog -> backlog the socket backlog. If this value is less than or equal to zero, then a system default value is used
        HttpServer httpServer = HttpServer.create(address, 0);
        
    }
}
