package com.codesoom.assignment.router;

import com.codesoom.assignment.TaskHttpHandler;
import com.codesoom.assignment.mapper.TaskMapper;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.Parser;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;

public class PutRouter {
    private final TaskService taskService;

    public PutRouter(TaskService taskService) {
        this.taskService = taskService;
    }

    public void handle(HttpExchange exchange, String path) throws IOException {
        String request = Parser.parsingRequest(exchange.getRequestBody());

        if (request.isBlank()) {
            TaskHttpHandler.sendResponse(exchange, HttpStatus.BAD_REQUEST, -1);
        }

        HashMap requestMap = TaskMapper.getRequestMap(request);
        Long findId = Parser.extractId(path.split("/"));

        Task changedTask = taskService.changeTask(findId, (String) requestMap.get("title"));

        if (changedTask == null) {
            TaskHttpHandler.sendResponse(exchange, HttpStatus.NOT_FOUND, -1);
            return;
        }

        String content = TaskMapper.taskToJson(changedTask);
        TaskHttpHandler.sendResponse(exchange, HttpStatus.OK, content.getBytes().length);
        TaskHttpHandler.writeResponseBody(exchange, content);
    }
}
