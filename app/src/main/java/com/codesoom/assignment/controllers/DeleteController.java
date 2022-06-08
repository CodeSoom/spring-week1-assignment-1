package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.Mapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteController {
    private final TaskService taskService;

    public DeleteController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path) throws IOException {
        if (PathParser.isReqDeleteOneTask(path)) {
            handleDeleteOneTask(exchange, path);
        } else {
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST, "This request can not be properly handled");
        }
    }

    private void handleDeleteOneTask(HttpExchange exchange, String path) throws IOException {
        Long id = PathParser.parseId(path);
        Task task = this.taskService.delete(id);

        if (task == null) {
            TaskController.sendResponse(exchange, HttpStatus.NOT_FOUND, "Task not found");
        }

        TaskController.sendResponse(exchange, HttpStatus.OK, Mapper.taskToString(task));
    }

}
