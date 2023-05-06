package com.codesoom.assignment.task.handler;

import com.codesoom.assignment.task.handler.request.HttpRequest;
import com.codesoom.assignment.task.handler.response.HttpResponse;
import com.codesoom.assignment.task.handler.route.TaskRouteHandler;
import com.codesoom.assignment.task.handler.route.impl.*;
import com.codesoom.assignment.task.repository.Tasks;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
        HttpRequest request = new HttpRequest(exchange);
        HttpResponse response = new HttpResponse(exchange);

        TaskRouteHandler taskRouteHandler = taskRouteHandlers.stream()
                .filter(handler -> handler.isSelect(request.getMethod(), request.getPath()))
                .findFirst()
                .orElse(taskRouteHandlers.get(taskRouteHandlers.size() - 1));

        taskRouteHandler.execute(request, response);
    }

}
