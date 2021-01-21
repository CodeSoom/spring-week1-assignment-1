package com.codesoom.assignment.web;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpStatusCode;
import com.codesoom.assignment.web.util.HttpResponseTransfer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteStrategy implements StrategyProcess {
    @Override
    public void process(HttpRequest httpRequest, HttpExchange httpExchange, TaskService taskService) throws IOException {
        long id = parseIdFromPath(httpRequest.getPath());
        taskService.deleteTask(id);
        HttpResponseTransfer.sendResponse(HttpStatusCode.NO_CONTENT, httpExchange);
    }

}
