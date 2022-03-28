package com.codesoom.assignment;

import com.codesoom.assignment.handler.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Task 에 대한 단일 조회, 목록 조회, 추가, 수정, 삭제를 제공하는 API 앱입니다.
 */
public class TaskApiApp {
    private final static int SERVER_PORT = 8000;
    private final static int BACK_LOG = 0;

    public static void main(String[] args) throws IOException {
        startLocalHttpServer();
    }

    public static void startLocalHttpServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress(SERVER_PORT);
        // backlog 파라미터는 listening socket 에 허용된 큐형식의 incoming connections 의 최대 개수를 말한다.
        HttpServer httpServer = HttpServer.create(address, BACK_LOG);
        HttpHandler handler = new TaskHttpHandler();

        httpServer.createContext("/tasks", handler);
        httpServer.start();
    }
}
