package com.codesoom.assignment;


import com.codesoom.assignment.models.RCode;
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
    private
    Long idCount =1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String num = path.replaceAll("[^0-9]", "");
        String body = makeBody(exchange);

        if (("GET").equals(method) && path.contains("/tasks")){
           send(exchange, getMethod(body, num), RCode.OK.getValue());
        }

        if(("POST").equals(method) && path.contains("/tasks")){
           send(exchange, postMethod(body, num), RCode.CREATE.getValue());
        }

        if(("PUT").equals(method) && path.contains("/tasks")) {
            send(exchange, putMethod(body, num), RCode.OK.getValue());
        }

        if(("DELETE").equals(method) && path.contains("/tasks")) {
            send(exchange, deleteMethod(num), RCode.OK.getValue());
        }

    }

    private String makeBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String body= new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return body;
    }

    private void send(HttpExchange exchange, String content, final int rCode) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        try {
            if(content.equals("NOT FOUND")){
                exchange.sendResponseHeaders(RCode.NOTFOUND.getValue(), content.getBytes().length);
            }else {
                exchange.sendResponseHeaders(rCode, content.getBytes().length);
            }
            outputStream.write(content.getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON(Object obj) throws IOException {
        if(obj==null){
            return "NOT FOUND";
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);
        return outputStream.toString();
    }

    private Task findTask(String num) {
        return tasks.stream()
                .filter(task -> task.getId().equals(Long.parseLong(num)))
                .findFirst()
                .orElse(null);
    }

    private String returnTaskOrTasks(String num) throws IOException {
       return num.isEmpty() ? tasksToJSON(tasks) : tasksToJSON(findTask(num));
    }

    public String getMethod( String body, String num) throws IOException {
            return body.isEmpty()? returnTaskOrTasks(num) : "GET은 조회만 가능합니다";
    }

    public String postMethod( String body, String num) throws IOException {
        Task task = toTask(body);
        task.setId(idCount++);
        tasks.add(task);
        return returnTaskOrTasks(num);
    }

    public String putMethod(String body, String num) throws IOException {
        Task task = findTask(num);
        if(task==null){
            return "NOT FOUND";
        }else {
            tasks.get(tasks.indexOf(task)).setTitle(toTask(body).getTitle());
            return returnTaskOrTasks(num);
        }
    }

    public String deleteMethod(String num) throws IOException {
        if(findTask(num)==null){
            return "NOT FOUND";
        }else {
            tasks.remove(findTask(num));
            return "삭제 되었습니다.";
        }
    }





}
