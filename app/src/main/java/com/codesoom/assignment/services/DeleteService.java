package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class DeleteService implements HttpRequestService {

    private static final DeleteService instance = new DeleteService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private DeleteService() {
    }

    public static DeleteService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, HttpExchange exchange) throws IOException {
        final String content = "";

        final Task removedTask = taskRepository.deleteById(id);

        if (removedTask == null) {
            return new HttpResponse(content, HttpStatusCode.NOT_FOUND);
        }

        return new HttpResponse(content, HttpStatusCode.NO_CONTENT);
    }
}
