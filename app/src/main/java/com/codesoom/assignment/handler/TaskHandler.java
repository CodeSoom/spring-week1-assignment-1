package com.codesoom.assignment.handler;

import com.codesoom.assignment.converter.TaskConverter;
import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpResponse;
import com.codesoom.assignment.exceptions.ParameterNotFoundException;
import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Task;
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
        final String body = getBody(exchange.getRequestBody());
        System.out.printf("[method] : %s , [path] : %s , [body] : %s%n", method , path.getFullPath() , body);

        String content = "";
        HttpResponse httpResponse = HttpResponse.OK;

        /*TODO
          if문 부분을 더 깔끔하게 할 수 있는 방법은 없을까?
          1. 서비스 계층을 추가하여 method , path , body를 넘겨 CRUD 작업을 맡기게 되면 ??

          2. 반복 되는 코드들을 어떻게 없앨 수 있을까 ?? 어떻게 하면 한 번에 이해 되는 코드를 짤 수 있을까?
         */
        if(path.hasResource() && path.resourceEquals("tasks")){
            if ("GET".equals(method.name())) {
                try {
                    Task task = tasks.get(Long.parseLong(path.getPathVariable()));
                    if(task == null){
                        httpResponse = HttpResponse.NOT_FOUND;
                    } else{
                        content = taskConverter.convert(task);
                    }
                } catch (ParameterNotFoundException e) {
                    content = taskConverter.convert(tasks);
                }
            } else if ("POST".equals(method.name())) {
                long taskId = getNextId();
                Task task = taskConverter.newTask(body , taskId);
                tasks.put(taskId, task);
                content = taskConverter.convert(task);
                httpResponse = HttpResponse.CREATED;
            } else if ("PUT".equals(method.name()) || "PATCH".equals(method.name())) {
                try {
                    long taskId = Long.parseLong(path.getPathVariable());
                    if(tasks.get(taskId) == null){
                        httpResponse = HttpResponse.NOT_FOUND;
                    } else{
                        Task task = taskConverter.newTask(body , taskId);
                        tasks.replace(taskId, task);
                        content = taskConverter.convert(task);
                    }
                }
                catch (ParameterNotFoundException e) {
                    httpResponse = HttpResponse.BAD_REQUEST;
                }
            } else if ("DELETE".equals(method.name())) {
                try {
                    long taskId = Long.parseLong(path.getPathVariable());
                    if(tasks.get(taskId) == null){
                        httpResponse = HttpResponse.NOT_FOUND;
                    } else{
                        tasks.remove(taskId);
                        httpResponse = HttpResponse.NO_CONTENT;
                    }
                }
                catch (ParameterNotFoundException e) {
                    httpResponse = HttpResponse.BAD_REQUEST;
                }
            }
        }

        exchange.sendResponseHeaders(httpResponse.getCode() , content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
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
