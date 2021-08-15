package com.codesoom.assignment;

import com.codesoom.assignment.models.Request;
import com.codesoom.assignment.models.RequestContent;
import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TasksStorage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private final TasksStorage tasks = new TasksStorage();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Response response = getResponse(new Request(path, method, body));

        exchange.sendResponseHeaders(response.getStatusCode(), response.getContentBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getContentBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Response getResponse(Request request) throws IOException {
        RequestMethod method = request.getRequestMethod();

        if (method == RequestMethod.GET && !request.isPathWithId()) {
            return handleGetRequest();
        }

        if (method == RequestMethod.GET && request.isPathWithId()) {
            return handleGetRequest(request.getPathId());
        }

        if (method == RequestMethod.POST) {
            return handlePostRequest(request.getRequestContent());
        }

        if (method == RequestMethod.PUT) {
            return handlePutRequest(request.getPathId(), request.getRequestContent());
        }

        if (method == RequestMethod.DELETE) {
            return handleDeleteRequest(request.getPathId());
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Response handleGetRequest() throws IOException {
        return new Response(tasks.readAll(), HttpStatus.OK);
    }

    private Response handleGetRequest(Long id) throws IOException {
        Optional<Task> task = tasks.read(id);

        if (task.isPresent()){
            return new Response(task.orElseThrow(), HttpStatus.OK);
        }

        return new Response(HttpStatus.NOT_FOUND);
    }

    private Response handlePostRequest(RequestContent content) throws IOException {
        return new Response(
                tasks.create(content.getTitle()),
                HttpStatus.CREATED
        );
    }

    private Response handlePutRequest(Long id, RequestContent content) throws IOException {
        Optional<Task> task = tasks.update(id, content.getTitle());

        if(task.isPresent()) {
            return new Response(task.orElseThrow(), HttpStatus.OK);
        }

        return new Response(HttpStatus.NOT_FOUND);
    }

    private Response handleDeleteRequest(Long id) throws IOException {
        Optional<Task> task = tasks.delete(id);

        if(task.isPresent()) {
            return new Response(task.orElseThrow(), HttpStatus.NO_CONTENT);
        }

        return new Response(HttpStatus.NOT_FOUND);
    }
}
