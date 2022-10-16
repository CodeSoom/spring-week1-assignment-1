package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;

public class DeleteService implements HttpRequestService {

    private static final DeleteService instance = new DeleteService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private DeleteService() {
    }

    public static DeleteService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, String requestBody) {
        final String emptyString = "";

        final Task removedTask = taskRepository.deleteById(id);

        if (removedTask == null) {
            return new HttpResponse(emptyString, HttpStatusCode.NOT_FOUND);
        }

        return new HttpResponse(emptyString, HttpStatusCode.NO_CONTENT);
    }
}
