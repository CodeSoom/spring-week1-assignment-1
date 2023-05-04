package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static com.codesoom.assignment.handler.HttpStatus.*;
import static com.codesoom.assignment.util.HttpExchangeUtil.sendHttpResponse;
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
    public void execute(final HttpExchange exchange) throws IOException {
        sendHttpResponse(exchange, OK.getCode(), objectToJsonString(tasks.getAll()));
    }

}
