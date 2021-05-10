package com.codesoom.assignment;

import com.codesoom.assignment.config.Config;
import com.codesoom.assignment.container.Container;
import com.codesoom.assignment.container.ControllerContainer;
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
        InetSocketAddress address = new InetSocketAddress(Config.HOST_NAME, Config.PORT_NUMBER);
        HttpServer httpServer = HttpServer.create(address,Config.BACKLOG);
        TaskRepository taskRepository = new TaskRepository();
        TaskController taskController = new TaskController(taskRepository);
        List<Controller> controllers = new ArrayList<>();
        controllers.add(taskController);
        Container container = new ControllerContainer(controllers);
        HttpHandler handler = new TaskHandler(container);

        httpServer.createContext(Config.ROOT_CONTEXT_PATH, handler);
        httpServer.start();
    }
}
