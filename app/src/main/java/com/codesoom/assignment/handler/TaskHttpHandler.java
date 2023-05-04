package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.task.model.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.codesoom.assignment.util.HttpExchangeUtil.getRequestMethod;
import static com.codesoom.assignment.util.HttpExchangeUtil.getRequestPath;

public class TaskHttpHandler implements HttpRequestHandler {

    private final List<TaskRouteHandler> taskRouteHandlers;

    public TaskHttpHandler() {
        Tasks tasks = new Tasks();
        taskRouteHandlers = Arrays.asList(
                new GetOneHandlerTask(tasks),
                new GetAllHandler(tasks),
                new PostHandler(tasks),
                new PutHandler(tasks),
                new DeleteHandler(tasks),
                new PathNotFoundHandler()
        );
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        TaskRouteHandler taskRouteHandler = taskRouteHandlers.stream()
                .filter(handler -> handler.isSelect(getRequestMethod(exchange), getRequestPath(exchange)))
                .findFirst()
                .orElse(taskRouteHandlers.get(taskRouteHandlers.size() - 1));
        taskRouteHandler.execute(exchange);
    }

}
