package com.codesoom.assignment;


import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();


    long id = 0L;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String content = "Hello codesoom";


        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String task_id = getId(path);
        System.out.println(task_id);


        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " "+ path);


        if(!body.isBlank() && method.equals("POST") ){

            Task task = toTask(body);
            ++id;
            task.setId(id);
            tasks.add(task);
        };


        //GET /tasks
        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        //GET tasks/{task_id}
        if (method.equals("GET") && path.equals("/tasks/"+task_id)) {
            Integer task_id_int = Integer.parseInt(task_id);
            Task target_task = tasks.get( task_id_int-1 );

            content = targetTaskToJson(target_task);
        }

        //POST tasks
        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task";
        }

        if (method.equals("PUT") && path.equals("/tasks/"+task_id)) {
            Integer task_id_int = Integer.parseInt(task_id);
            Task target_task = tasks.get( task_id_int-1 );
            Task change_task = toTask(body);
            target_task.setTitle(change_task.getTitle());

            content = "change target task";
        }


        if (method.equals("DELETE") && path.equals("/tasks/"+task_id)) {
            Integer task_id_int = Integer.parseInt(task_id);
            tasks.remove( task_id_int-1 );

            content = "Deleted target task";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private String getId(String path) {
        return path.replace("/tasks/","");
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


    private String targetTaskToJson(Task task) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
    private String tasksToJson() throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

}
