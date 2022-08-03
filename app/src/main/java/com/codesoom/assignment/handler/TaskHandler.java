package com.codesoom.assignment.handler;

import com.codesoom.assignment.enums.HttpResponse;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {

    public static List<Task> tasks = new LinkedList<>();
    public ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String[] path = exchange.getRequestURI().getPath().split("/");
        final String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                            .lines()
                            .collect(Collectors.joining("\n"));
        final int taskId = path.length >= 3 ? Integer.parseInt(path[2]) - 1 : 0;
        System.out.println(String.format("[method] : %s , [path] : %s , [body] : %s" , method , path[1] , body));

        String content = "";
        HttpResponse httpResponse = HttpResponse.OK;

        /*TODO
          path를 짜르고 안전하게 접근할 방법은 없을까?

          if문 부분을 더 깔끔하게 할 수 있는 방법은 없을까?
          1. 서비스 계층을 추가하여 method , path , body를 넘겨 CRUD 작업을 맡기게 되면 ??
          - 응답코드와 응답 바디에 담을 데이터를 같이 반환받게 되면 추가적인 if문이 필요한데
          - exchange를 같이 넘기면 ??
          - 서비스 계층에서 exchange를 조작하는 것은 이상하다

          예외는 어떻게 전달해줘야할까?
          1. taskId가 필요한데 없다면?
          2. body가 필요한데 없다면?
          3. 경로가 없다면?
         */
        if("tasks".equals(path[1])){
            if("GET".equals(method)){
                content = taskId == 0 ? allTaskToJson() : taskToJson(tasks.get(taskId - 1));
            }
            else if("POST".equals(method)){
                Task task = jsonToTask(body);
                task.setId((long) (tasks.size() + 1));
                tasks.add(task);
                content = taskToJson(task);
                httpResponse = HttpResponse.CREATED;
            }
            else if("PUT".equals(method) || "PATCH".equals(method)){
                Task newTask = jsonToTask(body);
                Task task = tasks.get(taskId);
                task.setTitle(newTask.getTitle());
                content = taskToJson(task);
            }
            else if("DELETE".equals(method)){
                tasks.remove(taskId);
            }
        }

        exchange.sendResponseHeaders(httpResponse.getCode() , content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        return mapper.readValue(content , Task.class);
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream , task);
        return outputStream.toString();
    }

    private String allTaskToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream , tasks);
        return outputStream.toString();
    }
}
