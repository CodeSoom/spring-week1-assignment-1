package com.codesoom.assignment.task.handler.crud;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.service.TaskService;
import com.codesoom.assignment.task.validator.TaskValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public abstract class CrudTaskHandler {

    protected final TaskService taskService;
    protected final TaskValidator taskValidator = new TaskValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CrudTaskHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    protected String getBody(HttpExchange httpExchange) {
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    protected Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    protected String toJson(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }

}
