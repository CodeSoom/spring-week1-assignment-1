package com.codesoom.assignment.web.service;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;

import java.io.IOException;

public class GetStrategy implements StrategyProcess {
    @Override
    public HttpResponse process(HttpRequest httpRequest, TaskService taskService) throws IOException {
        HttpResponse response;

        if (httpRequest.getPath().equals("/tasks") || httpRequest.getPath().equals("/tasks/")) {
            String responseJson = JsonUtil.toJson(taskService.getTasks());
            response = new HttpResponse(responseJson, HttpStatusCode.OK);
        } else {
            long id = parseIdFromPath(httpRequest.getPath());
            String responseJson = JsonUtil.toJson(taskService.getTask(id));
            response = new HttpResponse(responseJson, HttpStatusCode.OK);
        }
        return response;
    }

}
