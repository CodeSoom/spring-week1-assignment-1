package com.codesoom.assignment.task.handler.route.impl;

import com.codesoom.assignment.common.HttpOk;
import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;
import com.codesoom.assignment.task.repository.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.task.util.JsonUtil.objectToJsonString;

public class GetAllHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public GetAllHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("GET") && path.equals("/tasks");
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        HttpOk httpOk = new HttpOk(objectToJsonString(tasks.getAll()));
        response.send(httpOk);
    }

}
