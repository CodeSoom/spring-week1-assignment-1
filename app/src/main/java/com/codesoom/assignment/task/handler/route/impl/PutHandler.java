package com.codesoom.assignment.task.handler.route.impl;

import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;
import com.codesoom.assignment.task.model.Task;
import com.codesoom.assignment.task.repository.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.common.HttpStatus.OK;
import static com.codesoom.assignment.task.util.JsonUtil.jsonToObject;
import static com.codesoom.assignment.task.util.JsonUtil.objectToJsonString;
import static com.codesoom.assignment.task.util.TaskRoutePattern.TASK_ID_PATH_PATTERN;

public class PutHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public PutHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("PUT") && path.matches(TASK_ID_PATH_PATTERN);
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        Task task = tasks.update(
                request.parseIdFromPath(),
                jsonToObject(request.getBody(), Task.class).getTitle()
        );
        response.send(OK.getCode(), objectToJsonString(task));
    }

}
