package com.codesoom.assignment;

import com.codesoom.assignment.web.MyHandler;
import com.codesoom.assignment.web.MyHttpServer;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class App {
    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        MyHttpServer httpServer = new MyHttpServer(PORT);
        TaskService taskService = new TaskService();
        HttpHandler handler = new MyHandler(taskService);
        httpServer.addHandler("/", handler);
        httpServer.start();
    }

}
