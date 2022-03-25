package com.codesoom.assignment;

import com.codesoom.assignment.handlers.DemoHttpHandler;
import com.codesoom.assignment.handlers.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    private static final int PORT = 8000;
    private static final int BACK_LOG = 0;  // TCP 연결의 최대 값. 0 혹은 0보다 작을 경우 system default 값으로 설정된다.

    public static void main(String[] args) {
        try {

            final InetSocketAddress address = new InetSocketAddress(PORT);
            final HttpServer httpServer = HttpServer.create(address, BACK_LOG);

            final HttpHandler handler = new DemoHttpHandler();
            final TaskHttpHandler taskHttpHandler = new TaskHttpHandler();

            httpServer.createContext("/", handler);
            httpServer.createContext("/tasks", taskHttpHandler);

            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
