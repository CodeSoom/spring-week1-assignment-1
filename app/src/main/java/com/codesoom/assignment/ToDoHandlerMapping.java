package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToDoHandlerMapping implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        // TODO: Spring MVC의 HandlerMapping 의 아이디어를 따와서 설계했으나 함수의 생각보다 썩 맘에 들지는 않는 상태
        ToDoController toDoController = new ToDoController();
        if (isPathHasTasks(path)) {
            switch (requestMethod) {
                case "GET":
                    if (isPathHasTaskID(path)){
                        toDoController.getTasksByID(exchange, getTaskID(path));
                    } else {
                        toDoController.getTasks(exchange);
                    }
                    break;
                case "POST":
                    toDoController.createTask(exchange);
                    break;
                case "PUT":
                case "PATCH":
                    if (isPathHasTaskID(path)) {
                        toDoController.editTaskTitleByID(exchange, getTaskID(path));
                    }
                    break;
                case "DELETE":
                    if (isPathHasTaskID(path)){
                        toDoController.deleteTaskByID(exchange, getTaskID(path));
                    }
                    break;
                default:
                    throw new IOException("Unknown API Endpoint");
            }
        } else {
            throw new IOException("Unknown API Endpoint");
        }
    }

    private Boolean isPathHasTasks(String path){
        return path.matches("^/tasks/?$");
    }

    private Boolean isPathHasTaskID(String path){
        return path.matches("^/tasks/\\d+?$");
    }

    private Integer getTaskID(String path){
        Pattern pattern = Pattern.compile("^/tasks/(\\d+)?$");
        Matcher matcher = pattern.matcher(path);
        return Integer.parseInt(matcher.group(1));
    }
}
