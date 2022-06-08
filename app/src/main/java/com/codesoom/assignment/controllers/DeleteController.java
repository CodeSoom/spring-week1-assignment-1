package com.codesoom.assignment.controllers;

import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

public class DeleteController {
    private final TaskService taskService;

    public DeleteController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path) {
        if (PathParser.isReqDeleteOneTask(path)) {
            handleDeleteOneTask(exchange, path);
        } else {
            TaskController.sendResponse(exchange, 400, "This request can not be properly handled");
        }
    }

    private void handleDeleteOneTask(HttpExchange exchange, String path) {
        Long id = PathParser.parseId(path);
        this.taskService.delete(id);
    }

}
