package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.getRequestPath;
import static com.codesoom.assignment.util.HttpExchangeUtil.sendHttpResponse;
import static com.codesoom.assignment.util.JsonUtil.objectToJsonString;
import static com.codesoom.assignment.util.JsonUtil.parseIdFromPath;

public class GetOneHandlerTask implements TaskRouteHandler {

    private final Tasks tasks;

    public GetOneHandlerTask(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("GET") && path.matches("/tasks/\\d+");
    }

    @Override
    public void execute(final HttpExchange exchange) throws IOException {
        long id = parseIdFromPath(getRequestPath(exchange));
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(tasks.findById(id)));
    }

}
