package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private int idx;
    public TodoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("hello");
        tasks.add(task);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("world");
        tasks.add(task2);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String content = "";

        System.out.println(method + " " + path);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (method.equals("GET")) {
            if (hasNumberParameter(path))
                content = taskToJson(idx - 1);
             else
                content = taskToJson();
        } else if (method.equals("POST") && path.equals("/tasks")) {
            Task task = jsonToTask(body);
            tasks.add(task);
            content = taskToJson(task.getId() - 1);
        } else if (method.equals("PUT")) {
            if (hasNumberParameter(path)) {
                tasks.remove(idx-1);
                Task task = jsonToTask(body);
                task.setId((long) idx);
                tasks.add(idx-1,task);
                content = taskToJson(idx - 1);
            }
            else
                content = "Cannot be modified";
        }
        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
    private boolean hasNumberParameter(String path){
        if(path.startsWith("/tasks/")){
            String[] split = path.split("/");
            if (split.length == 3) {
                idx=Integer.parseInt(split[2]);
                return true;
            }
        }
        return false;
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId((long) (tasks.size() + 1));
        return task;
    }

    private String taskToJson() throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks);
        return outputstream.toString();
    }

    private String taskToJson(long idx) throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks.get((int) idx));
        return outputstream.toString();
    }
}
