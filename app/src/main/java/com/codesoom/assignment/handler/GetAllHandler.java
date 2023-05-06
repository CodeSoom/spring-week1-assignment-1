package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.OK;
import static com.codesoom.assignment.util.JsonUtil.objectToJsonString;

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
        response.send(OK.getCode(), objectToJsonString(tasks.getAll()));
    }

}
