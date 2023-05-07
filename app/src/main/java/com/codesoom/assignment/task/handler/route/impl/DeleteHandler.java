package com.codesoom.assignment.task.handler.route.impl;

import com.codesoom.assignment.common.OkStatus;
import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;
import com.codesoom.assignment.task.repository.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.task.util.TaskRoutePattern.TASK_ID_PATH_PATTERN;

public class DeleteHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public DeleteHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("DELETE") && path.matches(TASK_ID_PATH_PATTERN);
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        tasks.delete(request.parseIdFromPath());
        response.send(new OkStatus().getCode(), "");
    }

}
