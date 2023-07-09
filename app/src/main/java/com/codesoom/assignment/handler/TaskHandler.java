package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.repository.impl.TaskListRepositoryImpl;
import com.codesoom.assignment.vo.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TaskHandler {
    protected String method;
    protected String path;
    protected HttpExchange exchange;
    protected TaskRepository taskRepository = TaskListRepositoryImpl.getInstance();
    private Pattern pattern = Pattern.compile("/(\\d+)$");

    public TaskHandler(HttpExchange exchange) {
        this.exchange = exchange;
        this.method = HttpMethod.POST.name().equals(exchange.getRequestMethod()) ? HttpMethod.POST.name() : exchange.getRequestMethod();
        this.path = "/tasks";
    }

    public abstract boolean isRequest();

    protected Long parsePathToTaskId(String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }


    public abstract void handle() throws IOException;
}
