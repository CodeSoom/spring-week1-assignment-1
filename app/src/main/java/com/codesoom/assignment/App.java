package com.codesoom.assignment;

import com.codesoom.assignment.handlers.RootHttpHandler;
import com.codesoom.assignment.handlers.TaskHttpHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App {

    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        /**
         * Generate Java Server
         * */
        InetSocketAddress address = new InetSocketAddress(8000);
        HttpServer httpServer = HttpServer.create(address, 0);
        HttpHandler roothandler = new RootHttpHandler();
        HttpHandler taskHandler = new TaskHttpHandler();
        httpServer.createContext("/", roothandler);
        httpServer.createContext("/tasks", taskHandler);
        httpServer.start();
    }
}
