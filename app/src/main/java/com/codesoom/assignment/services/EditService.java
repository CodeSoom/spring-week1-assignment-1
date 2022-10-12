package com.codesoom.assignment.services;

import com.codesoom.assignment.HttpStatusCode;
import com.codesoom.assignment.utils.JsonConverter;
import com.codesoom.assignment.repository.TaskRepository;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class EditService implements HttpRequestService {

    private static final EditService instance = new EditService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private EditService() {
    }

    public static EditService getInstance() {
        return instance;
    }

    public String serviceRequest(Long id, HttpExchange exchange) throws IOException {
        String content;

        final String body = getRequestBody(exchange);
        final Task newTask = JsonConverter.toTask(body);

        final Task editedTask = taskRepository.editTaskById(id, newTask);
        if (editedTask == null) {
            content = "";
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, content.getBytes().length);
            return content;
        }

        content = JsonConverter.taskToJson(editedTask);
        exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);
        return content;
    }

    private String getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
