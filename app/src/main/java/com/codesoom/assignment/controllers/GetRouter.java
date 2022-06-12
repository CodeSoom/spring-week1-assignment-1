package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskManager;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.TaskMapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class GetRouter {
    private final TaskManager taskManager;

    public GetRouter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void route(HttpExchange exchange, String path) throws IOException {
        if (PathParser.isReqGettingOneTask(path)) {
            getTask(exchange, path);
        } else if (PathParser.isReqGettingAllTasks(path)) {
            getAllTasks(exchange, path);
        } else {
            rejectUnregisteredPath(exchange);
        }
    }

    private void rejectUnregisteredPath(HttpExchange exchange) {
        TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST.statusCode(), "This request can not be properly handled");
    }

    private void getTask(HttpExchange exchange, String path) throws IOException {
        Long id = PathParser.parseId(path) ;
        Task task = this.taskManager.show(id);

        if (task == null) {
            TaskController.sendResponse(exchange, HttpStatus.NOT_FOUND.statusCode(), "Task not found");
        }

        TaskController.sendResponse(exchange, HttpStatus.OK.statusCode(), TaskMapper.toString(task));
    }

    private void getAllTasks(HttpExchange exchange, String path) throws IOException {
        List<Task> tasks = this.taskManager.showAll();

        TaskController.sendResponse(exchange, HttpStatus.OK.statusCode(), TaskMapper.toString(tasks));
    }
}
