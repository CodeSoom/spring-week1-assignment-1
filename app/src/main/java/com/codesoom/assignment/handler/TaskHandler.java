package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.repository.impl.TaskListRepositoryImpl;
import com.codesoom.assignment.vo.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.NoSuchElementException;

public abstract class TaskHandler {
    protected String method;
    protected String path;
    protected HttpExchange exchange;
    protected TaskRepository taskRepository = TaskListRepositoryImpl.getInstance();

    public TaskHandler(HttpExchange exchange) {
        this.exchange = exchange;
        this.method = HttpMethod.POST.name().equals(exchange.getRequestMethod()) ? HttpMethod.POST.name() : exchange.getRequestMethod();
        this.path = "/tasks";
    }

    public abstract boolean isRequest();

    protected Long parsePathToTaskId(String path) {
        String[] splitUrl = path.split("/");
        Long id = Long.parseLong(splitUrl[splitUrl.length - 1]);
        return id;
    }


    public abstract void handle() throws IOException;
}
