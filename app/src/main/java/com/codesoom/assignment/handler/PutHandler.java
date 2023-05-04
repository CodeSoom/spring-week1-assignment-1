package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Task;
import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.*;
import static com.codesoom.assignment.util.JsonUtil.*;

public class PutHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public PutHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("PUT") && path.matches("/tasks/\\d+");
    }

    @Override
    public void execute(final HttpExchange exchange) throws IOException {
        Task task = tasks.update(
                parseIdFromPath(getRequestPath(exchange)),
                jsonToObject(extractRequestBody(exchange), Task.class).getTitle()
        );
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(task));
    }

}
