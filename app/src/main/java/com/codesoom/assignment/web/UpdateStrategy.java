package com.codesoom.assignment.web;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class UpdateStrategy implements StrategyProcess {
    @Override
    public void process(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException {
        long id = parseIdFromPath(httpRequest.getPath());
        Task task = JsonUtil.toTask(httpRequest.getBody());
        Task updatedTask = taskService.updateTask(id, task.getTitle());
        String responseJson = JsonUtil.toJson(updatedTask);
        HttpResponseTransfer.sendResponse(responseJson, HttpStatusCode.OK, httpExchange);
    }

}
