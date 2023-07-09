package com.codesoom.assignment.handler.impl;

import com.codesoom.assignment.handler.TaskHandler;
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

public class GetTaskHandler extends TaskHandler {
    private final Pattern pathPatternRegex = Pattern.compile("^/tasks/\\d+$");

    public GetTaskHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    public void handle() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            Long id = parsePathToTaskId(exchange.getRequestURI().getPath());

            Task task = taskRepository.findById(id);
            String findTask = parseTaskToJsonString(task);
            bufferedOutputStream.write(findTask.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(findTask);
        } catch (NoSuchElementException e) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            exchange.close();
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public boolean isRequest() {
        URI uri = exchange.getRequestURI();
        return HttpMethod.GET.name().equals(method) && pathPatternRegex.matcher(uri.getPath()).matches();
    }
}
