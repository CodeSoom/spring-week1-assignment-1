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

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler(){
        Task task = new Task();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] splits = path.split("/");

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        if(!body.isBlank()){
            if(method.equals("POST")){
                Task task = toTask(body);
                tasks.add(task);
                System.out.println(task);
            }else if(method.equals("PUT") || method.equals("PATCH")){
                String newTitle = body.substring(11, body.length()-2);
                updateById(splits[2], newTitle);
            }
        }

        String content = "Hello, world!";

        if(method.equals("GET")){
            content = tasksToJson();
        }

        if(method.equals("POST")){
            content = tasksToJson();
        }

        if(method.equals("PUT") || method.equals("PATCH")){
            content = tasksToJson();
        }

        if(method.equals("DELETE")){
            deleteById(splits[2]);
            content = tasksToJson();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException{

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private void updateById(String id, String newTitle){

        for(Task task: tasks){
            if(task.getId() == Long.parseLong(id)){
                task.setTitle(newTitle);
            }
        }
    }

    private void deleteById(String id){
         for(Task task: tasks){
             if(task.getId() == Long.parseLong(id)){
                 tasks.remove(task);
             }
         }
    }

}
