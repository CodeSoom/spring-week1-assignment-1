package com.codesoom.assignment;

import com.codesoom.assignment.controller.Controller;
import com.codesoom.assignment.controller.TaskController;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException{
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        HttpServer httpServer = HttpServer.create(address,0);
        TaskController taskController = new TaskController();
        List<Controller> controllers = new ArrayList<>();
        controllers.add(taskController);
        HttpHandler handler = new TaskHandler(controllers);

        httpServer.createContext("/", handler); // 주어진 경로로 들어오는 요청을 handler에서 처리하겠다.
        httpServer.start();
    }
}
