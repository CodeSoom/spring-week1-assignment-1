package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.utils.JsonConverter;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;

import java.io.IOException;

public class EditService implements HttpRequestService {

    private static final EditService instance = new EditService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private EditService() {
    }

    public static EditService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, String requestBody) throws IOException {
        String content;

        final String newTitle = JsonConverter.jsonToTitle(requestBody);

        final Task editedTask = taskRepository.editTaskById(id, newTitle);
        if (editedTask == null) {
            content = "";
            return new HttpResponse(content, HttpStatusCode.NOT_FOUND);
        }

        content = JsonConverter.taskToJson(editedTask);
        return new HttpResponse(content, HttpStatusCode.OK);
    }

}
