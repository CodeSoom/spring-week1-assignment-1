package com.codesoom.assignment.task.handler.route;

import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;

import java.io.IOException;

public interface TaskRouteHandler {

    boolean isSelect(String method, String path);

    void execute(HttpRequest request, HttpResponse response) throws IOException;

}
