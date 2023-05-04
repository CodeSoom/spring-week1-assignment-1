package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.getRequestPath;
import static com.codesoom.assignment.util.HttpExchangeUtil.sendHttpResponse;
import static com.codesoom.assignment.util.JsonUtil.parseIdFromPath;

public class DeleteHandler implements TaskRouteHandler {

    private final Tasks tasks;

    public DeleteHandler(final Tasks tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean isSelect(final String method, final String path) {
        return method.equals("DELETE") && path.matches("/tasks/\\d+");
    }

    @Override
    public void execute(final HttpExchange exchange) throws IOException {
        tasks.delete(parseIdFromPath(getRequestPath(exchange)));
        sendHttpResponse(exchange, OK.getCode(), "");
    }

}
