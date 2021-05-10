package com.codesoom.assignment.handler;

import com.codesoom.assignment.HttpStatus;
import com.codesoom.assignment.Response;
import com.codesoom.assignment.controller.Controller;
import com.codesoom.assignment.container.Container;
import com.codesoom.assignment.exception.ControllerNotFoundException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private final Container container;

    public TaskHandler(Container container) {
        this.container = container;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Response response = this.container.resolve(exchange);
            sendResponse(exchange, response);
        } catch (ControllerNotFoundException e) {
            Response response = new Response(HttpStatus.NOT_FOUND);
            sendResponse(exchange, response);
        }
    }

    private void sendResponse(HttpExchange exchange, Response response) throws IOException {
        exchange.sendResponseHeaders(response.getStatus(), response.getBody().getBytes(StandardCharsets.UTF_8).length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(response.getBody().getBytes(StandardCharsets.UTF_8));
        responseBody.close();
    }
}
