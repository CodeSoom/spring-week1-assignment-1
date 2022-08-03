package com.codesoom.assignment.impl;

import com.codesoom.assignment.IAssignmentController;
import com.codesoom.assignment.IHttphandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName AssignmentHttpHandler
 * @Description TODO에 대한 API를 핸들링하는 클래스이다.
 * @method handle handle Http request
 * @params HttpExchange exchange
 * @return void
 */

public class AssignmentHttpHandler implements IHttphandler {

    private static final IAssignmentController controller = new AssignmentController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        AtomicReference<String> content = new AtomicReference<>("Hello World!");

        if (method.equals("GET") && path.equals("/tasks")) {
            controller.getAll(exchange);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            InputStream requestBody = exchange.getRequestBody();
            controller.create(exchange, requestBody);
        }
    }
}
