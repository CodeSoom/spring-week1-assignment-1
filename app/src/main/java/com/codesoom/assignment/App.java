package com.codesoom.assignment;

import com.codesoom.assignment.handler.HttpRequestHandler;
import com.codesoom.assignment.handler.TaskHttpHandler;
import com.codesoom.assignment.server.SimpleHttpServer;

public class App {

    public static void main(String[] args) {
        HttpRequestHandler httpRequestHandler = new TaskHttpHandler();
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(httpRequestHandler);
        simpleHttpServer.run();
    }

}
