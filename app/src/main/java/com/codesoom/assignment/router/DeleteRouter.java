package com.codesoom.assignment.router;

import com.codesoom.assignment.TaskHttpHandler;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.service.Parser;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteRouter {
    private final TaskService taskService;

    public DeleteRouter(TaskService taskService) {
        this.taskService = taskService;
    }

    public void handle(HttpExchange exchange, String path) throws IOException {
        boolean result = taskService.deleteTask(Parser.extractId(path.split("/")));

        if (result) {
            TaskHttpHandler.sendResponse(exchange, HttpStatus.NO_CONTENT, -1);
            return;
        }

        TaskHttpHandler.sendResponse(exchange, HttpStatus.NOT_FOUND, -1);
    }
}
