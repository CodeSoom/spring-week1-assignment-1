package com.codesoom.assignment.web.service;

import com.codesoom.assignment.service.TaskService;
import com.codesoom.assignment.web.models.HttpRequest;
import com.codesoom.assignment.web.models.HttpResponse;

import java.io.IOException;

public interface RequestControllable {
    HttpResponse process(HttpRequest httpRequest) throws IOException;
}
