package com.codesoom.assignment.handler;

import com.codesoom.assignment.converter.TaskConverter;
import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpResponse;
import com.codesoom.assignment.exceptions.ParameterNotFoundException;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {

    public static Map<Long , Task> tasks = new ConcurrentHashMap<>();
    public static AtomicLong taskId = new AtomicLong(1L);
    public TaskConverter taskConverter = TaskConverter.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        final Path path = new Path(exchange.getRequestURI().getPath());
        System.out.printf("[method] : %s , [path] : %s%n", method , path.fullPath);
        if(!path.hasResource() || !path.resourceEquals("tasks")) {
            send(exchange, HttpResponse.OK.getCode() , "");
        }

        if(path.hasPathVariable()){
            handleUsingTaskId(exchange , method , Long.parseLong(path.pathVariable));
        }
        else{
            handleTask(exchange , method);
        }
    }

    private void handleUsingTaskId(HttpExchange exchange, HttpMethod method , Long id) throws IOException {
        String content = "";
        Task task = tasks.get(id);
        if(task == null){
            send(exchange , HttpResponse.NOT_FOUND.getCode(), content);
            return;
        }

        HttpResponse response = HttpResponse.OK;
        if ("GET".equals(method.name())) {
            content = taskConverter.convert(task);
        } else if ("PUT".equals(method.name()) || "PATCH".equals(method.name())) {
            content = updateTask(exchange, id);
        } else if ("DELETE".equals(method.name())) {
            tasks.remove(id);
            response = HttpResponse.NO_CONTENT;
        }

        send(exchange , response.getCode(), content);
    }

    private void handleTask(HttpExchange exchange, HttpMethod method) throws IOException {
        String content = "";
        HttpResponse response = HttpResponse.OK;
        if ("GET".equals(method.name())) {
            content = taskConverter.convert(tasks);
        } else if ("POST".equals(method.name())) {
            content = createTask(exchange);
            response = HttpResponse.CREATED;
        }
        send(exchange, response.getCode() , content);
    }

    private String createTask(HttpExchange exchange) throws IOException {
        long taskId = getNextId();
        Task task = taskConverter.newTask(getBody(exchange.getRequestBody()) , taskId);
        tasks.put(taskId, task);
        return taskConverter.convert(task);
    }

    private void send(HttpExchange exchange, int code, String content) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type" , "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(code , content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String updateTask(HttpExchange exchange, Long id) {
        String content;
        Task newTask = taskConverter.newTask(getBody(exchange.getRequestBody()) , id);
        tasks.replace(id, newTask);
        content = taskConverter.convert(newTask);
        return content;
    }

    private String getBody(InputStream inputStream){
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private long getNextId(){
        return taskId.getAndIncrement();
    }
}
