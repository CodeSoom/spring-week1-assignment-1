package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.utils.JsonConverter;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class PostService implements HttpRequestService {

    private static final PostService instance = new PostService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private PostService() {
    }

    public static PostService getInstance() {
        return instance;
    }

    public HttpResponse serviceRequest(Long id, HttpExchange exchange) throws IOException {
        String content;

        final String body = getRequestBody(exchange);
        final Task newTask = JsonConverter.toTask(body);
        taskRepository.addNewTask(newTask);

        content = JsonConverter.taskToJson(newTask);
        return new HttpResponse(content, HttpStatusCode.CREATED);
    }

    private String getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
