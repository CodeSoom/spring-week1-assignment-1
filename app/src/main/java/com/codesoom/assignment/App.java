package com.codesoom.assignment;

import com.codesoom.assignment.task.handler.HttpRequestHandler;
import com.codesoom.assignment.task.handler.TaskHttpHandler;
import com.codesoom.assignment.task.server.SimpleHttpServer;

public class App {

    public static void main(String[] args) {
        HttpRequestHandler httpRequestHandler = new TaskHttpHandler();
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(httpRequestHandler);
        simpleHttpServer.run();
    }

}
