package com.codesoom.assignment;

import com.codesoom.assignment.controller.TasksController;
import com.codesoom.assignment.models.HttpRequest;
import com.codesoom.assignment.models.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class TasksHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());

        TasksReflection tasksReflection = new TasksReflection();
        HttpResponse httpResponse = (HttpResponse) tasksReflection.processMethod(TasksController.class, httpRequest);

        sendResponse(exchange, httpResponse);
    }

    private void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
