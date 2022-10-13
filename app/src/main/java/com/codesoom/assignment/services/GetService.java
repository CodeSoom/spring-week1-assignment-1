package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.utils.JsonConverter;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;

import java.io.IOException;
import java.util.Collection;

public class GetService implements HttpRequestService {

    private static final GetService instance = new GetService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private GetService() {
    }

    public static GetService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, String requestBody) throws IOException {
        String content;

        if (id == null) {
            Collection<Task> allTasks = taskRepository.getAllTasks();
            content = JsonConverter.tasksToJson(allTasks);
            return new HttpResponse(content, HttpStatusCode.OK);
        }

        Task task = taskRepository.findById(id);
        if (task == null) {
            content = "";
            return new HttpResponse(content, HttpStatusCode.NOT_FOUND);
        }

        content = JsonConverter.taskToJson(task);
        return new HttpResponse(content, HttpStatusCode.OK);
    }
}
