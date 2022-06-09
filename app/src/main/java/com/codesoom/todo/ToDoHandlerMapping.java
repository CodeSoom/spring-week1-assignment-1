package com.codesoom.todo;

import com.codesoom.todo.controllers.ToDoController;
import com.codesoom.todo.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class ToDoHandlerMapping implements HttpHandler {
    TaskService taskService = new TaskService();
    ToDoController toDoController = new ToDoController(taskService);
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();


        switch (requestMethod) {
            case "GET" -> getMapper(exchange, path, toDoController);
            case "POST" -> postMapper(exchange, path, toDoController);
            case "PUT" -> putMapper(exchange, path, toDoController);
            case "PATCH" -> patchMapper(exchange, path, toDoController);
            case "DELETE" -> deleteMapper(exchange, path, toDoController);
            default -> toDoController.errorResponse(); // 405
        }

    }

    private void getMapper(HttpExchange exchange, String path, ToDoController toDoController) {
        if (isPathMatches("^/tasks/?$", path)) {
            toDoController.getTasks(exchange);
        } else if (isPathMatches("^/tasks/\\d+/?$", path)) {
            toDoController.getTask(exchange, getTaskID(path));
        } else {
            toDoController.errorResponse(); // 405
        }
    }

    private void postMapper(HttpExchange exchange, String path, ToDoController toDoController) throws IOException {
        if (isPathMatches("^/tasks/?$", path)) {
            toDoController.createTask(exchange);
        } else {
            toDoController.errorResponse(); // 405
        }
    }

    private void putMapper(HttpExchange exchange, String path, ToDoController toDoController) {
        if (isPathMatches("^/tasks/\\d+/?$", path)) {
            toDoController.getTask(exchange, getTaskID(path));
        } else {
            toDoController.errorResponse(); // 405
        }
    }

    private void patchMapper(HttpExchange exchange, String path, ToDoController toDoController) {
        if (isPathMatches("^/tasks/\\d+/?$", path)) {
            toDoController.getTask(exchange, getTaskID(path));
        } else {
            toDoController.errorResponse(); // 405
        }
    }

    private void deleteMapper(HttpExchange exchange, String path, ToDoController toDoController) {
        if (isPathMatches("^/tasks/\\d+/?$", path)) {
            toDoController.getTask(exchange, getTaskID(path));
        } else {
            toDoController.errorResponse(); // 405
        }
    }

    private Boolean isPathMatches(String pattern, String path) {
        return path.matches(pattern);
    }

    private Long getTaskID(String path) {
        return Long.parseLong(path.split("/")[2]);
    }
}
