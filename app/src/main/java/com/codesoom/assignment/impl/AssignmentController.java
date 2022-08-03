package com.codesoom.assignment.impl;

import com.codesoom.assignment.IAssignmentController;
import com.codesoom.assignment.IAssignmentService;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class AssignmentController implements IAssignmentController {

    private static final IAssignmentService service = new AssignmentService();

    public void create(HttpExchange exchange, InputStream inputStream) throws IOException {
        String body = toBody(inputStream);
        bodyIsNotNullOrThrow(body, exchange);
        Optional<Task> task = service.create(body);
        if (task.isPresent()) {
            response(exchange, String.valueOf(task), 201);
        } else {
            response(exchange, "", 400);
        }
    }

    public void getAll(HttpExchange exchange) throws IOException {
        List<Task> tasks = service.getAll();
        System.out.println(tasks);
        response(exchange, String.valueOf(tasks), 200);
    }
}
