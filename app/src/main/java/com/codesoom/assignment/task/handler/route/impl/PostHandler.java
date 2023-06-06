package com.codesoom.assignment.task.handler.route.impl;

import com.codesoom.assignment.common.HttpCreated;
import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;
import com.codesoom.assignment.task.model.Task;
import com.codesoom.assignment.task.repository.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.task.util.JsonUtil.jsonToObject;
import static com.codesoom.assignment.task.util.JsonUtil.objectToJsonString;

public class PostHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public PostHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("POST") && path.equals("/tasks");
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        tasks.add(jsonToObject(request.getBody(), Task.class));
        HttpCreated httpCreated = new HttpCreated(objectToJsonString(tasks.getAll()));
        response.send(httpCreated);
    }

}
