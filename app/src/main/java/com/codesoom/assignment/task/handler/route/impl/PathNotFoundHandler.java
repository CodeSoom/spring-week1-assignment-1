package com.codesoom.assignment.task.handler.route.impl;

import com.codesoom.assignment.common.HttpNotFound;
import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;

import java.io.IOException;


public class PathNotFoundHandler implements TaskRouteHandler {

    @Override
    public boolean isSelect(final String method, final String path) {
        return false;
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        String errorMessage = String.format(
                "404 Not Found: The requested path '%s' with method '%s' does not exist.",
                request.getPath(),
                request.getMethod()
        );
        HttpNotFound httpNotFound = new HttpNotFound(errorMessage);
        response.send(httpNotFound);
    }

}
