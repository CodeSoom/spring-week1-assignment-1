package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Task 에 대한 단일 조회, 목록 조회, 추가, 수정, 삭제를 제공하는 API 앱입니다.
 */
public class TaskApiApp {
    public static void main(String[] args) throws IOException {
        final int SERVER_PORT = 8000;

        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
        // backlog 파라미터는 listening socket 에 허용된 큐형식의 incoming connections 의 최대 개수를 말한다.
        HttpServer httpServer = HttpServer.create(address, 0);
        HttpHandler handler = new TaskHttpHandler();

        httpServer.createContext("/", handler);
        httpServer.start();
    }
}
