package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final Integer PORT = 8080;
    //backlog: maximum number of queued incoming connections to allow on the listening socket.
    private static final Integer BACKLOG = 0; // 0으로 설정 시 시스템의 default값 사용

    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
            httpServer.createContext("/tasks", new TaskHttpHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
