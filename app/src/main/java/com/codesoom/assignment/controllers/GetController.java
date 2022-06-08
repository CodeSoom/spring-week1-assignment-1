package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.Mapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class GetController {
    private final TaskService taskService;

    public GetController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path) throws IOException {
        if (PathParser.isReqGettingOneTask(path)) {
            handleGetOneTask(exchange, path);
        } else if (PathParser.isReqGettingAllTasks(path)) {
            handleGetAllTasks(exchange, path);
        } else {
            rejectUnregisteredPath(exchange);
        }
    }

    private void rejectUnregisteredPath(HttpExchange exchange) {
        TaskController.sendResponse(exchange, 400, "This request can not be properly handled");
    }

    private void handleGetOneTask(HttpExchange exchange, String path) throws IOException {
        Long id = PathParser.parseId(path) ;
        Task task = this.taskService.show(id);

        if (task == null) {
            TaskController.sendResponse(exchange, 404, "Task not found");
        }

        TaskController.sendResponse(exchange, 200, Mapper.taskToString(task));
    }

    private void handleGetAllTasks(HttpExchange exchange, String path) throws IOException {
        List<Task> tasks = this.taskService.showAll();
//        if (tasks.isEmpty()) {
//            TaskController.sendResponse(exchange, 404, "There's no registered tasks");
//        }
//
        TaskController.sendResponse(exchange, 200, Mapper.taskToString(tasks));
    }
}
