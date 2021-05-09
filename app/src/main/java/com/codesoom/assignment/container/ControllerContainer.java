package com.codesoom.assignment.container;

import com.codesoom.assignment.HttpStatus;
import com.codesoom.assignment.Response;
import com.codesoom.assignment.controller.Controller;
import com.codesoom.assignment.exception.ControllerNotFoundException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class ControllerContainer extends Container{

    private final List<Controller> controllers;

    public ControllerContainer(List<Controller> controllers) {
        this.controllers = controllers;
    }

    @Override
    public Response resolve(HttpExchange exchange) throws IOException {
        try {
            Controller controller = this.getControllerByPath(exchange.getRequestURI().getPath());
            return controller.resolve(exchange);
        } catch (ControllerNotFoundException e) {
            return new Response(HttpStatus.NOT_FOUND);
        }
    }

    private Controller getControllerByPath(String path) throws ControllerNotFoundException {
        return this.controllers.stream()
                .filter((controller) -> controller.handleResource(path))
                .findFirst()
                .orElseThrow(ControllerNotFoundException::new);
    }
}
