package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private final TaskFactory taskFactory = new TaskFactory();
    private final HttpResponse httpResponse = new HttpResponse(taskFactory);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final String body = createBody(exchange);

        HttpRequest httpRequest = new HttpRequest(method, path);
        System.out.println(method + " " + path);

        String id = checkPathGetId(path);

        httpResponse.setHttpStatusCode(HttpStatus.NOT_FOUND.getCode());
        httpResponse.setContent("");

        // GET /tasks
        if(httpRequest.isGetAllTasks()) {
            System.out.println("1 = " + 1);
            httpResponse.getAllTasks();
        }

        // GET /tasks/{id}
        if(httpRequest.isGetOneTask()) {
            System.out.println("2 = " + 2);
            httpResponse.getOneTask(id);
        }

        // POST /tasks
        if(httpRequest.isCreateTask()){
            System.out.println("3 = " + 3);
            httpResponse.createTask(body);
        }

        // PUT,PATCH /tasks/{id}
        if(httpRequest.isUpdateTask()) {
            System.out.println("4 = " + 4);
            httpResponse.updateTask(id, body);
        }

        // Delete /tasks/{id}
        if(httpRequest.isDeleteTask()) {
            System.out.println("5 = " + 5);
            httpResponse.deleteTask(id);
        }
        
        exchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getContent().getBytes().length);

        OutputStream outputstream = exchange.getResponseBody();
        outputstream.write(httpResponse.getContent().getBytes());
        outputstream.flush();
        outputstream.close();
    }

    private String createBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return body;
    }

    private String checkPathGetId(String path) {
        if (path.indexOf("/tasks/") == 0) {
            return path.substring("/tasks/".length());
        }
        return "";
    }

}
