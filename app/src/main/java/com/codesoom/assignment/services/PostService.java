package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.JsonConverter;
import com.codesoom.assignment.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class PostService {

    private static final PostService instance = new PostService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private PostService() {
    }

    public static PostService getInstance() {
        return instance;
    }

    public String service(HttpExchange exchange) throws IOException {
        String content;

        final String body = getRequestBody(exchange);
        final Task newTask = JsonConverter.toTask(body);
        taskRepository.addNewTask(newTask);

        content = JsonConverter.taskToJson(newTask);
        exchange.sendResponseHeaders(HttpStatusCode.CREATED.code, content.getBytes().length);
        return content;
    }

    private String getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
