package com.codesoom.assignment.web.service;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;

import java.io.IOException;

public class GetRequestController implements RequestControllable {
    @Override
    public HttpResponse process(HttpRequest httpRequest, TaskService taskService) throws IOException {
        HttpResponse response;

        if (isRequestTaskList(httpRequest.getPath())) {
            String responseJson = JsonUtil.toJson(taskService.getTasks());
            response = new HttpResponse(responseJson, HttpStatusCode.OK);
        } else {
            long id = parseIdFromPath(httpRequest.getPath());
            String responseJson = JsonUtil.toJson(taskService.getTask(id));
            response = new HttpResponse(responseJson, HttpStatusCode.OK);
        }
        return response;
    }

    private boolean isRequestTaskList(String path) {
        return path.equals("/tasks") || path.equals("/tasks/");
    }

}
