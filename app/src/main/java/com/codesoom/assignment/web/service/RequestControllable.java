package com.codesoom.assignment.web.service;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;

import java.io.IOException;

public interface RequestControllable {
    HttpResponse process(HttpRequest httpRequest, TaskService taskService) throws IOException;

    default long parseIdFromPath(String path) throws NumberFormatException {
        //path 마지막에 '/'이 붙어 있을 것을 대비
        String idString = path.replace("/tasks/", "").replace("/", "");
        return Long.parseLong(idString);
    }

}
