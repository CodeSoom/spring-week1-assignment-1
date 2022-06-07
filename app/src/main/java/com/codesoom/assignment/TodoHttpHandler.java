package com.codesoom.assignment;

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
        if(method.equals("GET")&& path.equals("/tasks")){
            content = todoService.getTasks();
        }else if(method.equals("POST")&& path.equals("/tasks")){
            content = todoService.postTask(body);
        }else if(method.equals("PUT")&& path.matches("/tasks/[0-9\\w]+")){
            taskId = Long.parseLong(path.split("/")[2]);
            content = todoService.putTasks(taskId,body);
        }else if(method.equals("GET")&& path.matches("/tasks/[0-9\\w]+")){
            taskId = Long.parseLong(path.split("/")[2]);
            content = todoService.getTask(taskId);
        }else if(method.equals("DELETE")&& path.matches("/tasks/[0-9\\w]+")){
            taskId = Long.parseLong(path.split("/")[2]);
            content = todoService.deleteTask(taskId);
        }else{

        }

        exchange.sendResponseHeaders(200,content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }
}
