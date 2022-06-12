package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskManager;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.TaskMapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteRouter {
    private final TaskManager taskManager;

    public DeleteRouter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void route(HttpExchange exchange, String path) throws IOException {
        if (PathParser.isReqDeleteOneTask(path)) {
            deleteTask(exchange, path);
        } else {
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST.statusCode(), "This request can not be properly handled");
        }
    }

    private void deleteTask(HttpExchange exchange, String path) throws IOException {
        Long id = PathParser.parseId(path);
        Task task = this.taskManager.delete(id);
        if (task == null) {
            TaskController.sendResponse(exchange, HttpStatus.NOT_FOUND.statusCode(), "Task not found");
        }

        TaskController.sendResponse(exchange, HttpStatus.NO_CONTENT.statusCode(), TaskMapper.toString(task));
    }

}
