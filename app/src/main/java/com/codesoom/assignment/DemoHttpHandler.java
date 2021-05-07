package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long index;

    public DemoHttpHandler(){
//        Task task = new Task();
//        task.setTitle("First");
//        task.setId(1L);
//        tasks.add(task);
//
//        Task task2 = new Task();
//        task2.setTitle("Second");
//        task2.setId(2L);
//        tasks.add(task2);
        index = 1L;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE
        // 2. Path - "/", "/tasks", "/tasks/1" ...
        // 3. Headers, Body(Content)

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));


        // System.out.println("---start---");
        System.out.println(method +" " + path);
        int rCode = 500;
        String content = "Hello world!";

        // GET
        if (method.equals("GET") && path.equals("/")){
            content = tasksToJSON();
            rCode = 200;
        }

        // GET
        if (method.equals("GET") && path.contains("/tasks")){
            if (path.equals("/tasks") || path.equals("/tasks/")){
                System.out.println(tasks);
            }else{
                String[] split = path.split("/");
                Long idx = Long.parseLong(split[split.length-1]);
                System.out.println(tasks.get(idx.intValue()-1));
            }

            content = tasksToJSON();
            rCode = 200;
        }

        // POST
        if (method.equals("POST") && path.equals("/tasks")){
            if(!body.isEmpty()){
                Task task = toTask(body);
                setIndex(task);
                System.out.println(task);
                tasks.add(task);
            }
            content = "create new tasks";
            rCode = 201;
        }

        // PUT/PATCH
        if ((method.equals("PUT") || method.equals("PATCH")) && path.contains("/tasks")){
            String[] split = path.split("/");
            Long idx = Long.parseLong(split[split.length-1]);
            if(body.isEmpty()){
               throw new IOException("Plz fill body");
            }
            Task new_task = toTask(body);
            new_task.setId(idx);
            System.out.println("new_task = " + new_task);

            //tasks.get(Integer.parseInt(split[1])).setId(new_task.getId());
            tasks.get(Integer.parseInt(String.valueOf(idx))-1).setTitle(new_task.getTitle());

            System.out.println("validate = " + tasks.get(Integer.parseInt(String.valueOf(idx))-1));  // validate

            content = tasksToJSON();
            rCode = 200;
        }

        // DELETE
        if (method.equals("DELETE") && path.contains("/tasks")){
            String[] split = path.split("/");
            Long idx = Long.parseLong(split[split.length-1]);
            if(body.isEmpty()){
                throw new IOException("Plz fill body");
            }
            tasks.remove(Integer.parseInt(String.valueOf(idx))-1);

            content = tasksToJSON();
            rCode = 200;
        }

        exchange.sendResponseHeaders(rCode,content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void setIndex(Task task) {
        task.setId(index);
        index += 1;
    }

    private Task toTask(String content) throws IOException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(byteArrayOutputStream,tasks);

        return byteArrayOutputStream.toString();
    }
}
