package com.codesoom.assignment.http;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TaskHttpHandler implements HttpHandler {
    private static final String TASK_PATH = "/tasks";
    private static final String PATH_SPLITTER = "/";
    private static final int ID_POSITION = 2;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(
                httpExchange.getRequestMethod(),
                httpExchange.getRequestURI(),
                httpExchange.getRequestBody());

        if (httpRequest.path().startsWith(TASK_PATH)) {
            try {
                send(httpExchange, handleMethod(httpRequest));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private HttpResponse handleMethod(HttpRequest httpRequest) throws IOException, ClassNotFoundException {
        String method = httpRequest.method().toString();
        Long id = getIdFromPath(httpRequest.path());

        if (RequestMethod.GET.toString().equals(method)) {
            if (id != null) {
                return new HttpResponse(HttpResponse.STATUS_OK,
                        objectMapper.writeValueAsString(TaskManager.find(id)));
            } else {
                return new HttpResponse(HttpResponse.STATUS_OK,
                        objectMapper.writeValueAsString(TaskManager.findAll()));
            }
        }

        if (RequestMethod.POST.toString().equals(method)) {
            Task task = toTask(httpRequest.body());
            return new HttpResponse(HttpResponse.STATUS_CREATED,
                    toJSON(TaskManager.insert(task)));
        }

        if (id == null) {
            return new HttpResponse(HttpResponse.STATUS_BAD_REQUEST, "Not include id in path");
        }

        if (RequestMethod.PATCH.toString().equals(method) ||
                RequestMethod.PUT.toString().equals(method)) {
            Task task = toTask(httpRequest.body());
            return new HttpResponse(HttpResponse.STATUS_OK,
                    TaskManager.modify(id, task).toString());
        }

        if (RequestMethod.DELETE.toString().equals(method)) {
            TaskManager.delete(id);
            return new HttpResponse(HttpResponse.STATUS_OK, "");
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

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
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
