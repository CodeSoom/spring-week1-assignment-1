package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.utils.JsonConverter;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;

import java.io.IOException;

public class PostService implements HttpRequestService {

    private static final PostService instance = new PostService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private PostService() {
    }

    public static PostService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, String requestBody) throws IOException {
        String content;

        final String title = JsonConverter.jsonToTitle(requestBody);
        Task newTask = taskRepository.addNewTask(title);

        content = JsonConverter.taskToJson(newTask);
        return new HttpResponse(content, HttpStatusCode.CREATED);
    }

}
