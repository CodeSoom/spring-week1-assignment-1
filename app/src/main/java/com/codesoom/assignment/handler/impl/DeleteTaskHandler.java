package com.codesoom.assignment.handler.impl;

import com.codesoom.assignment.handler.TaskHandler;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.vo.HttpMethod;
import com.codesoom.assignment.vo.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class DeleteTaskHandler extends TaskHandler {
    private final Pattern pathPatternRegex = Pattern.compile("^/tasks/\\d+$");

    public DeleteTaskHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    public boolean isRequest() {
        URI uri = exchange.getRequestURI();
        return HttpMethod.DELETE.name().equals(method) && pathPatternRegex.matcher(uri.getPath()).matches();
    }

    @Override
    public void handle() throws IOException {
        try {
            Long id = parsePathToTaskId(exchange.getRequestURI().getPath());
            Task task = taskRepository.findById(id);
            taskRepository.delete(task);
            exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getCode(), 0);
            exchange.close();
        } catch (NoSuchElementException e) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
            throw e;
        }
    }
}
