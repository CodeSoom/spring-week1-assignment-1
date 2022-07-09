package com.codesoom.assignment.router;

import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Router {
    private final TaskService taskService = new TaskService();
    private final PostRouter postRouter = new PostRouter(taskService);
    private final PutRouter putRouter = new PutRouter(taskService);
    private final DeleteRouter deleteRouter = new DeleteRouter(taskService);
    private final GetRouter getRouter = new GetRouter(taskService);

    public void getHandle(HttpExchange exchange, String path) throws IOException {
        getRouter.handle(exchange, path);
    }

    public void postHandle(HttpExchange exchange) throws IOException {
        postRouter.handle(exchange);
    }

    public void putHandle(HttpExchange exchange, String path) throws IOException {
        putRouter.handle(exchange, path);
    }

    public void deleteHandle(HttpExchange exchange, String path) throws IOException {
        deleteRouter.handle(exchange, path);
    }
}
