package com.codesoom.assignment.web.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.util.JsonUtil;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;

import java.io.IOException;

public class UpdateRequestController implements RequestControllable {
    @Override
    public HttpResponse process(HttpRequest httpRequest, TaskService taskService) throws IOException {
        long id = parseIdFromPath(httpRequest.getPath());
        Task task = JsonUtil.toTask(httpRequest.getBody());
        Task updatedTask = taskService.updateTask(id, task.getTitle());
        String responseJson = JsonUtil.toJson(updatedTask);
        return new HttpResponse(responseJson, HttpStatusCode.OK);
    }

}
