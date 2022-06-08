package com.codesoom.assignment.handler;

import com.codesoom.assignment.Exception.NotFoundException;
import com.codesoom.assignment.TodoService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private TodoService todoService = new TodoService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String content = "Hello World";
        Long taskId = 0L;
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        System.out.println(method + " " + path);
        try{
            if (method.equals("GET") && path.equals("/tasks")) {
                content = todoService.getTasks();
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else if (method.equals("POST") && path.equals("/tasks")) {
                content = todoService.postTask(body);
                exchange.sendResponseHeaders(201, content.getBytes().length);
            } else if (method.equals("PUT") && path.matches("/tasks/[0-9\\w]+")) {
                taskId = Long.parseLong(path.split("/")[2]);
                content = todoService.putTask(taskId, body);
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else if (method.equals("GET") && path.matches("/tasks/[0-9\\w]+")) {
                taskId = Long.parseLong(path.split("/")[2]);
                content = todoService.getTask(taskId);
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else if (method.equals("DELETE") && path.matches("/tasks/[0-9\\w]+")) {
                taskId = Long.parseLong(path.split("/")[2]);
                content = todoService.deleteTask(taskId);
                exchange.sendResponseHeaders(204, content.getBytes().length);
            } else {
               throw new NotFoundException("존재하지 않는 API");
            }
        }catch(NotFoundException e){
            content = e.getMessage();
            exchange.sendResponseHeaders(404, content.getBytes().length);
        }



        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
