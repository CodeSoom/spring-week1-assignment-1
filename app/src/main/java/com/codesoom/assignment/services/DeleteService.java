package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteService {

    private static final DeleteService instance = new DeleteService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private DeleteService() {
    }

    public static DeleteService getInstance() {
        return instance;
    }

    public String service(Long id, HttpExchange exchange) throws IOException {
        final String content = "";

        final Task removedTask = taskRepository.deleteById(id);

        if (removedTask == null) {
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, content.getBytes().length);
            return content;
        }

        exchange.sendResponseHeaders(HttpStatusCode.NO_CONTENT.code, -1);
        return content;
    }
}
