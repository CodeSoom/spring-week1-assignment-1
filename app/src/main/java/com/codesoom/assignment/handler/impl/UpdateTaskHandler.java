package com.codesoom.assignment.handler.impl;

import com.codesoom.assignment.handler.TaskHandler;
import com.codesoom.assignment.model.RequestBody;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.vo.HttpMethod;
import com.codesoom.assignment.vo.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static com.codesoom.assignment.utils.ParseUtil.parseTaskToJsonString;

public class UpdateTaskHandler extends TaskHandler {
    private final Pattern pathPatternRegex = Pattern.compile("^/tasks/\\d+$");

    public UpdateTaskHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    public void handle() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Long id = parsePathToTaskId(exchange.getRequestURI().getPath());
            Task task = taskRepository.findById(id);
            RequestBody requestBody = new RequestBody(exchange);
            Task updateTask = requestBody.read(Task.class);
            Task updatedTask = taskRepository.update(task, updateTask);
            String jsonUpdatedTask = parseTaskToJsonString(updatedTask);

            bufferedOutputStream.write(jsonUpdatedTask.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(jsonUpdatedTask);
        } catch (NoSuchElementException e) {
            e.getStackTrace();
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public boolean isRequest() {
        URI uri = exchange.getRequestURI();
        return (HttpMethod.PATCH.name().equals(method) || HttpMethod.PUT.name().equals(method)) && pathPatternRegex.matcher(uri.getPath()).matches();
    }
}
