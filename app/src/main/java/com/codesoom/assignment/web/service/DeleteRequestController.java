package com.codesoom.assignment.web.service;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;
import com.codesoom.assignment.web.models.HttpStatusCode;

import java.io.IOException;

public class DeleteRequestController implements RequestControllable {
    @Override
    public HttpResponse process(HttpRequest httpRequest, TaskService taskService) throws IOException {
        long id = parseIdFromPath(httpRequest.getPath());
        taskService.deleteTask(id);
        return new HttpResponse(HttpStatusCode.NO_CONTENT);
    }

}
