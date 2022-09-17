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
    private static Long idCount =1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String num = path.replaceAll("[^0-9]", "");
        String content = "";

        InputStream inputStream = exchange.getRequestBody();
        String title= new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));


        if(path.contains("/tasks")){
            content=workToMethod(method, title, num);

        }

        exchange.sendResponseHeaders(returnRCode(method), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON(Object obj) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);
        return outputStream.toString();
    }

    private Task findTask(String num) {
        for (Task t : tasks) {
            if (t.getId() == Long.parseLong(num)) {
                return t;
            }
        }
        return new Task(0L,"");
    }

    private String setTitle(Task task,String body, String num) throws IOException {
        tasks.get(tasks.indexOf(task)).setTitle(toTask(body).getTitle());
        return returnTaskOrTasks(num);
    }

    private String returnTaskOrTasks(String num) throws IOException {
       return num.isEmpty() ? tasksToJSON(tasks) : tasksToJSON(findTask(num));
    }

    private String workToMethod(String method, String title, String num) throws IOException {
        if (("GET").equals(method)){
            return title.isBlank() ? returnTaskOrTasks(num) : "GET은 조회만 가능합니다";
        }

        if(("POST").equals(method)){
            Task task = toTask(title);
            task.setId(idCount++);
            tasks.add(task);
            return returnTaskOrTasks(num);
        }

        if(("PUT").equals(method)) {
            return checkNum(num) ? setTitle(findTask(num), title, num) : "존재하지 않는 id입니다.";
        }

        if(("DELETE").equals(method)) {
            tasks.remove(findTask(num));
            return "";
        }

        return "";
    }

    public boolean checkNum(String num) {
        List<Long> TaskIds = tasks.stream().map(i -> i.getId()).collect(Collectors.toList());
        for (Long id:TaskIds) {
           return num.equals(String.valueOf(id));
        }
        return false;
    }

    public int returnRCode(String method) {
        if("POST".equals(method)){
            return 201;
        }
        return 200;
    }

}
