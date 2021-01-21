package com.codesoom.assignment.models;

import com.codesoom.assignment.service.TaskService;

public interface HttpRequestProcessor {
    HttpResponse processTasks(HttpRequest httpRequest, TaskService taskService);
}
