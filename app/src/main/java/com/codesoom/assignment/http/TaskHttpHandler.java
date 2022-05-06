package com.codesoom.assignment.http;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class TaskHttpHandler implements HttpHandler {
    private static final String TASK_PATH = "/tasks";
    private static final String PATH_SPLITTER = "/";
    private static final int ID_POSITION = 2;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(
                exchange.getRequestMethod(),
                exchange.getRequestURI(),
                exchange.getRequestBody());

        if (httpRequest.path().startsWith(TASK_PATH)) {
            HttpResponse httpResponse = null;
            try {
                httpResponse = handleMethod(httpRequest);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            send(exchange, httpResponse);
        }
    }

    private HttpResponse handleMethod(HttpRequest httpRequest) throws IOException, ClassNotFoundException {
        String method = httpRequest.method().toString();
        Long id = getIdFromPath(httpRequest.path());

        if (method.equals(RequestMethod.GET.toString())) {
            String content;
            if (id != null) {
                content = objectMapper.writeValueAsString(TaskManager.find(id));
            } else {
                content = objectMapper.writeValueAsString(TaskManager.findALl());
            }
            return new HttpResponse(HttpResponse.STATUS_OK, content);
        }

        if (method.equals(RequestMethod.POST.toString())) {
            Task task = toTask(httpRequest.body());
            Task newTask = TaskManager.insert(task);
            return new HttpResponse(HttpResponse.STATUS_CREATED, newTask.toString());
        }

        return new HttpResponse(HttpResponse.STATUS_NOT_FOUND, "Not Found");
    }

    private Long getIdFromPath(String path) {
        String[] pathArr = path.split(PATH_SPLITTER);
        if(pathArr.length < ID_POSITION + 1){ return null;}
        return Long.parseLong(pathArr[ID_POSITION]);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private void send(HttpExchange exchange, HttpResponse httpResponse) throws IOException {
        exchange.sendResponseHeaders(httpResponse.getStatusCode(), httpResponse.getContent().getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(httpResponse.getContent().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
