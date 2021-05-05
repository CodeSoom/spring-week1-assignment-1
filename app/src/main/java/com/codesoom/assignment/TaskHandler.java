package com.codesoom.assignment;

import com.codesoom.assignment.controller.Controller;
import com.codesoom.assignment.controller.TaskController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private List<Controller> controllers;
    public TaskHandler(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        exchange.sendResponseHeaders(200, 0);
//        OutputStream responseBody = exchange.getResponseBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        responseBody.write();
//        responseBody.close();
        String requestMethod = exchange.getRequestMethod();
        Controller controller = getControllerByPath(exchange.getRequestURI().getPath());

        switch (requestMethod) {
            case "GET" -> System.out.println("GET");
            case "POST" -> System.out.println("POST");
            case "PUT" -> System.out.println("PUT");
            case "PATCH" -> System.out.println("PATCH");
            case "DELETE" -> System.out.println("DELETE");
        }
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
    }

    private Controller getControllerByPath(String path) {
        for (Controller controller: controllers) {
            if (controller.handleResource(path)) return controller;
        }
        return null;
    }
}
