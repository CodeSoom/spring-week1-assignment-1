package com.codesoom.assignment;

import com.codesoom.assignment.controller.Controller;
import com.codesoom.assignment.controller.TaskController;
import com.codesoom.assignment.handler.TaskHandler;
import com.codesoom.assignment.repository.TaskRepository;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException{
        InetSocketAddress address = new InetSocketAddress("localhost", 8000);
        HttpServer httpServer = HttpServer.create(address,0);
        TaskRepository taskRepository = new TaskRepository();
        TaskController taskController = new TaskController(taskRepository);
        List<Controller> controllers = new ArrayList<>();
        controllers.add(taskController);
        HttpHandler handler = new TaskHandler(controllers);

        httpServer.createContext("/", handler);
        httpServer.start();
    }
}
