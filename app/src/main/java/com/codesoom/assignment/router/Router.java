package com.codesoom.assignment.router;

import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Router {
    private final TaskService taskService;
    private final PostRouter postRouter;
    private final PutRouter putRouter;
    private final DeleteRouter deleteRouter;
    private final GetRouter getRouter;

    public Router(TaskService taskService) {
        this.taskService = taskService;
        postRouter = new PostRouter(taskService);
        putRouter = new PutRouter(taskService);
        deleteRouter = new DeleteRouter(taskService);
        getRouter = new GetRouter(taskService);
    }

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
