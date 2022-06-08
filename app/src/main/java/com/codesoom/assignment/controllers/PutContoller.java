package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.Mapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PutContoller {
    private final TaskService taskService;

    public PutContoller(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path, String body) throws IOException {
        if (PathParser.isReqModifyOneTask(path)) {
            handleModifyOneTask(exchange, path, body);
        } else {
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST, "This request can not be properly handled");
        }
    }

    private void handleModifyOneTask(HttpExchange exchange, String path, String body) throws IOException {
        Long id = PathParser.parseId(path);
        Task task = Mapper.stringToTask(body);
        task.setId(id);
        Task modifiedTask = this.taskService.modify(task);

        if (modifiedTask == null) {
            TaskController.sendResponse(exchange, HttpStatus.NOT_FOUND, "Task not found");
        }

        TaskController.sendResponse(exchange, HttpStatus.OK, Mapper.taskToString(modifiedTask));

    }

}
