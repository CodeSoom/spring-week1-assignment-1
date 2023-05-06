package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.OK;
import static com.codesoom.assignment.util.JsonUtil.objectToJsonString;
import static com.codesoom.assignment.util.TaskRoutePattern.TASK_ID_PATH_PATTERN;

public class GetOneHandlerTask implements TaskRouteHandler {

    private final Tasks tasks;

    public GetOneHandlerTask(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("GET") && path.matches(TASK_ID_PATH_PATTERN);
    }

    @Override
    public void execute(final HttpRequest request, final HttpResponse response) throws IOException {
        long id = request.parseIdFromPath();
        response.send(OK.getCode(), objectToJsonString(tasks.findById(id)));
    }

}
