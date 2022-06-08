package com.codesoom.assignment.controllers;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.services.TaskService;
import com.codesoom.assignment.utils.Mapper;
import com.codesoom.assignment.utils.PathParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

public class PutContoller {
    private final TaskService taskService;

    public PutContoller(TaskService taskService) {
        this.taskService = taskService;
    }

    public void route(HttpExchange exchange, String path, String body) throws JsonProcessingException {
        if (PathParser.isReqModifyOneTask(path)) {
            handleModifyOneTask(exchange, body);
        } else {
            TaskController.sendResponse(exchange, 400, "This request can not be properly handled");
        }
    }

    private void handleModifyOneTask(HttpExchange exchange, String body) throws JsonProcessingException {
        Task task = Mapper.stringToTask(body);
        this.taskService.modify(task);
    }

}
