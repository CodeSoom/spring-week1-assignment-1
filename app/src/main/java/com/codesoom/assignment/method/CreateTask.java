package com.codesoom.assignment.method;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseCreated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class CreateTask extends DoTask {
    private static Long id = 1L;
    public CreateTask(HttpExchange exchange) {
        super(exchange);
    }

    public void handleItem() throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
                .collect(Collectors.joining("\n"));
        if (!body.isEmpty()) {
            Task task = contentToTask(body);
            tasks.add(task);
        }
        String content = tasksToJSON();
        new ResponseCreated(exchange).send(content);
    }

    @Override
    protected Task contentToTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(id);
        id += 1;
        return task;
    }
}
