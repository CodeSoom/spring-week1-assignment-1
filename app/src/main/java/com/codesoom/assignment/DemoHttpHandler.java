package com.codesoom.assignment;

import com.codesoom.assignment.models.*;
import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {

    private TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequestForTasks(exchange.getRequestMethod(), exchange.getRequestURI(), exchange.getRequestBody());

        if (!httpRequest.isValidMethod()) {
            sendResponse(exchange, new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED));
            return;
        }

        if (!httpRequest.isValidPath()) {
            sendResponse(exchange, new HttpResponse(HttpStatus.NOT_FOUND));
            return;
        }

        HttpRequestMethod httpRequestMethod = httpRequest.getMethod();
        HttpResponse response = httpRequestMethod.createResponse(httpRequest, taskService);
        sendResponse(exchange, response);
    }

    private void sendResponse(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }

}
