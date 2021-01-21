package com.codesoom.assignment.web;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class PostStrategy implements StrategyProcess {
    @Override
    public void process(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException {
        Task task = JsonUtil.toTask(httpRequest.getBody());
        Task createdTask = taskService.createNewTask(task.getTitle());
        String responseJson = JsonUtil.toJson(createdTask);
        HttpResponseTransfer.sendResponse(responseJson, HttpStatusCode.CREATED, httpExchange);
    }

}
