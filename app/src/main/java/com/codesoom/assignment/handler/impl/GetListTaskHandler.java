package com.codesoom.assignment.handler.impl;

import com.codesoom.assignment.handler.TaskHandler;
import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.response.ResponseSuccess;
import com.codesoom.assignment.vo.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codesoom.assignment.utils.ParseUtil.parseTaskListToJsonString;

public class GetListTaskHandler extends TaskHandler {
    public GetListTaskHandler(HttpExchange exchange) {
        super(exchange);
    }

    @Override
    public void handle() throws IOException {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(exchange.getResponseBody())) {
            List<Task> taskList = taskRepository.findAll();
            String jsonTaskList = parseTaskListToJsonString(taskList);
            bufferedOutputStream.write(jsonTaskList.getBytes(StandardCharsets.UTF_8));
            new ResponseSuccess(exchange).send(jsonTaskList);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public boolean isRequest() {
        URI uri = exchange.getRequestURI();
        return HttpMethod.GET.name().equals(method) && path.equals(uri.getPath());
    }
}
