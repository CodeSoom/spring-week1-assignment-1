package com.codesoom.assignment.http;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskHttpHandler implements HttpHandler {
    private static final String TASK_PATH = "/tasks";
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(
                exchange.getRequestMethod(),
                exchange.getRequestURI(),
                exchange.getRequestBody());

        if (httpRequest.getPath().equals(TASK_PATH)) {
            HttpResponse httpResponse = handleMethod(exchange, httpRequest);
            send(exchange, httpResponse);
        }
    }

    private HttpResponse handleMethod(HttpExchange exchange, HttpRequest httpRequest) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals(RequestMethod.GET.toString())) {
            List<Task> tasks = TaskManager.findALl();
            String content = objectMapper.writeValueAsString(tasks);
            return new HttpResponse(HttpResponse.STATUS_OK, content);
        }

        if (method.equals(RequestMethod.POST.toString())) {
            // todo: ConnectionError
            Task task = toTask(httpRequest.getBody());
            Task newTask = TaskManager.insert(task);
            return new HttpResponse(HttpResponse.STATUS_CREATED, newTask.toString());
        }

        return new HttpResponse(HttpResponse.STATUS_NOT_FOUND, "Not found");
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private void send(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getContent().getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(httpResponse.getContent().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
