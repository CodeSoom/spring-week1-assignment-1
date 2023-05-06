package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Task;
import com.codesoom.assignment.domain.task.model.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.CREATED;
import static com.codesoom.assignment.util.JsonUtil.jsonToObject;
import static com.codesoom.assignment.util.JsonUtil.objectToJsonString;

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
        response.send(CREATED.getCode(), objectToJsonString(tasks.getAll()));
    }

}
