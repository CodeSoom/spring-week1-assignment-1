package com.codesoom.assignment;

import com.codesoom.assignment.enums.ContextContainer;
import com.codesoom.assignment.enums.HttpMethodType;
import com.codesoom.assignment.error.ClientError;
import com.codesoom.assignment.models.Path;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FrontController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethodType method = HttpMethodType.getMethod(exchange.getRequestMethod());
        Path path = createPath(exchange);
        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
        ContextContainer context = ContextContainer.getContext(path);

        switch (context) {
            case TASKS:
                TaskController taskController = TaskController.getInstance();
                taskController.execute(exchange, method, path, requestBody);
                break;
            default:
                ClientError.notFound(exchange);
        }
    }

    private Path createPath(HttpExchange exchange) {
        Path path = new Path();
        String[] pathArr = exchange.getRequestURI().getPath().split("/");

        if (hasPath(pathArr)) {
            path.setPath(pathArr[1]);
        }

        if (hasPathVariable(pathArr)) {
            path.setPathVariable(pathArr[2]);
        }

        return path;
    }

    private boolean hasPathVariable(String[] pathArr) {
        return pathArr.length > 2;
    }

    private boolean hasPath(String[] pathArr) {
        return pathArr.length > 0;
    }
}
