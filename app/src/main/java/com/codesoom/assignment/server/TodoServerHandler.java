package com.codesoom.assignment.server;

import com.codesoom.assignment.domain.task.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TodoServerHandler implements HttpHandler {
    public final TaskService taskService;
    public TodoServerHandler(TaskService taskService){
        this.taskService=taskService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
