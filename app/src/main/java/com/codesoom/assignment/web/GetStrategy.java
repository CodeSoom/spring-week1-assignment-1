package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class GetStrategy implements StrategyProcess {
    @Override
    public void process(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException {
        if (httpRequest.getPath().equals("/tasks") || httpRequest.getPath().equals("/tasks/")) {
            String responseJson = JsonUtil.toJson(taskService.getTasks());
            HttpResponseTransfer.sendResponse(responseJson, HttpStatusCode.OK, httpExchange);
        } else {
            long id = parseIdFromPath(httpRequest.getPath());
            String responseJson = JsonUtil.toJson(taskService.getTask(id));
            HttpResponseTransfer.sendResponse(responseJson, HttpStatusCode.OK, httpExchange);
        }
    }

}
