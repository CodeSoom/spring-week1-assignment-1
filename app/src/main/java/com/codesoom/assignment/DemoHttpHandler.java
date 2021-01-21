package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

enum Status {
    OK(200), CREATED(201), NO_CONTENT(204), BAD_REQUEST(400), NOT_FOUND(404);

    private final int status;
    Status(int status) {
        this.status = status;
    }

    int getStatus(){
        return this.status;
    }
}

public class DemoHttpHandler implements HttpHandler {
    Controller controller = new Controller();
    Service service = new Service();
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private OutputStream outputStream;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method+" "+path);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "Hello World";

        switch (method) {
            case "GET" -> controller.requestForGet(path, tasks, exchange);
            case "POST" -> controller.requestForPost(path, body, tasks, exchange);
            case "PUT", "PATCH" -> controller.requestForPutOrPatch(path, body, tasks, exchange);
            case "DELETE" -> controller.requestForDelete(path, tasks, exchange);
        }

//        if(method.equals("GET")) {
//            requestForGet(path, exchange);
//        }
//
//        else if(method.equals("POST")) {
//            requestForPost(path, body, exchange);
//        }
//
//        else if((method.equals("PUT") || method.equals("PATCH"))){
//            requestForPutOrPatch(path, body, exchange);
//        }
//
//        else if(method.equals("DELETE")) {
//            requestForDelete(path, exchange);
//        }
    }

    /*public void requestForGet(String path, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        if(path.equals("/tasks")) {
            content = (tasks == null ) ? "[]" : tasksToJson();
            exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
            writeContentWithOutputStream(exchange, content);
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task getTask = service.getTask(idValue, tasks);

        if(getTask == null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        content = taskToJson(getTask);
        exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
        writeContentWithOutputStream(exchange, content);
    }

    private void requestForPost(String path, String body, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        String requestTitle = body.split("\"")[1];

        if(!requestTitle.equals("title")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        Task task = jsonToTask(body);
        service.createTask(task, tasks);
        content = taskToJson(task);
        exchange.sendResponseHeaders(Status.CREATED.getStatus(),content.getBytes().length);
        writeContentWithOutputStream(exchange, content);
    }

    private void requestForPutOrPatch(String path, String body, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task updateTask = service.getTask(idValue, tasks);

        if(updateTask == null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(), 0);
            writeContentWithOutputStream(exchange, content);
            return;
        }

        Task task = jsonToTask(body);
        service.updateTask(updateTask, task.getTitle());
        content = taskToJson(updateTask);
        exchange.sendResponseHeaders(Status.OK.getStatus(),content.getBytes().length);
        writeContentWithOutputStream(exchange, content);
    }

    private void requestForDelete(String path, HttpExchange exchange) throws IOException {
        String content = "";
        if(!path.startsWith("/tasks")) {
            exchange.sendResponseHeaders(Status.BAD_REQUEST.getStatus(),0);
            writeContentWithOutputStream(exchange, "");
            return;
        }

        Long idValue = Long.parseLong(path.substring(7));
        Task deleteTask = service.getTask(idValue, tasks);

        if(deleteTask==null) {
            exchange.sendResponseHeaders(Status.NOT_FOUND.getStatus(), 0);
            writeContentWithOutputStream(exchange, content);
            return;
        }

        service.deleteTask(deleteTask, tasks);
        content = "";
        exchange.sendResponseHeaders(Status.NO_CONTENT.getStatus(),0);
        writeContentWithOutputStream(exchange, content);
    }*/

    /*private void writeContentWithOutputStream(HttpExchange exchange, String content) throws IOException {
        outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task jsonToTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    private String tasksToJson() throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }*/
}
