package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskManager;
import com.codesoom.assignment.utils.HttpStatus;
import com.codesoom.assignment.utils.TaskMapper;
import com.codesoom.assignment.utils.PathParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PutRouter {
    private final TaskManager taskManager;

    public PutRouter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void route(HttpExchange exchange, String path, String body) throws IOException {
        if (PathParser.isReqModifyOneTask(path)) {
            modifyTask(exchange, path, body);
        } else {
            TaskController.sendResponse(exchange, HttpStatus.BAD_REQUEST.statusCode(), "This request can not be properly handled");
        }
    }

    private void modifyTask(HttpExchange exchange, String path, String body) throws IOException {
        Long id = PathParser.parseId(path);
        Task task = TaskMapper.toTask(body);
        task.setId(id);
        Task modifiedTask = this.taskManager.modify(task);

        if (modifiedTask == null) {
            TaskController.sendResponse(exchange, HttpStatus.NOT_FOUND.statusCode(), "Task not found");
        }

        TaskController.sendResponse(exchange, HttpStatus.OK.statusCode(), TaskMapper.toString(modifiedTask));

    }

}
