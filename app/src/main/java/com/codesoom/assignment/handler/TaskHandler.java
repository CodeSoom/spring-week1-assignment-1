package com.codesoom.assignment.handler;

import com.codesoom.assignment.Response;
import com.codesoom.assignment.controller.Controller;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private final List<Controller> controllers;
    public TaskHandler(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Controller controller = getControllerByPath(exchange.getRequestURI().getPath());

        assert controller != null;
        Response res = controller.resolve(exchange);
        exchange.sendResponseHeaders(res.getStatus(), res.getBody().getBytes(StandardCharsets.UTF_8).length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(res.getBody().getBytes(StandardCharsets.UTF_8));
        responseBody.close();
    }

    private Controller getControllerByPath(String path) {
        for (Controller controller: controllers) {
            if (controller.handleResource(path)) return controller;
        }
        return null;
    }
}
