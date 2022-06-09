package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.Mapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PostController {
    private final TaskService taskService;

    public PostController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path, String body) throws IOException {
        if (PathParser.isReqRegisterOneTask(path)) {
            registerTask(exchange, body);
        } else {
            // FIXME - Controller마다 생기는 중복을 어떻게 하면 줄일 수 있을까?
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST.statusCode(), "This request can not be properly handled");
        }
    }

    private void registerTask(HttpExchange exchange, String body) throws IOException {
        Task task = Mapper.stringToTask(body);
        Task registeredTask = this.taskService.register(task);
        if (registeredTask == null) {
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST.statusCode(), "Duplicated id");
        }

        TaskController.sendResponse(exchange, HttpStatus.CREATED.statusCode(), Mapper.taskToString(task));
    }
}
