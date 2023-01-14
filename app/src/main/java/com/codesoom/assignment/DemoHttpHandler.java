package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Rest Api를 만들어 보자.
 * 1. 목록 얻기 - GET /tasks
 * 2. 상세 조회하기 - GET /tasks/{id}
 * 3. 생성 하기 - POST /tasks
 * 4. 제목 수정하기 - PUT/PATCH /tasks/{id}
 * 5. 삭제하기하기 - DELETE /tasks/{id}
 *
 * id를 어떻게 분리 시켜야 하는가?
 * */
public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    private Long id = 1L;
    private final Pattern urlPattern = Pattern.compile("/tasks/(\\d)");

    public DemoHttpHandler(){

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        Long requestId = getId(path);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "Hello, world!";

        if (method.equals("GET") && path.equals("/tasks")){
            System.out.println("Get list.");
            content = tasksToJSON();
        }

        if (method.equals("POST") && path.contains("/tasks") && !body.isBlank()){
            System.out.println("Create a task");

            makeTask(body);

            content = tasksToJSON();
        }

        if ((method.equals("PUT") || method.equals("PATCH")) && path.contains("/tasks") && !body.isBlank()){
            System.out.println("Update a task");

            updateTask(requestId, body);
            content = tasksToJSON();
        }

        if (method.equals("DELETE") && path.contains("/tasks")){
            System.out.println("Delete a task");

            deleteTask(requestId);
            content = tasksToJSON();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    //body의 내용을 task 객체에 넣어준다.
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }


    //tasks를 json형태로 보여준다.
    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        
        return outputStream.toString();
    }

    //task를 등록한다.
    private void makeTask(String content) throws JsonProcessingException {
        Task task = toTask(content);
        task.setId(id++);
        tasks.add(task);
    }

    //요청 id를 가져온다.
    private Long getId(String url){
        Matcher matcher = urlPattern.matcher(url);
        if(matcher.find()){
            return Long.parseLong(matcher.group(1));
        }

        return null;
    }

    //요청 id에 대한 task를 확인하고 update 처리한다
    private void updateTask(Long id, String content) throws JsonProcessingException {
        Task task = tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);

        if(task != null){
            task.setTitle(toTask(content).getTitle());
        }
    }

    private void deleteTask(Long id){
        Task task = tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);

        if(task != null){
            tasks.remove(task);
        }
    }
}
