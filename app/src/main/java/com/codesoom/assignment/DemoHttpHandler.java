package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpStatus;
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

        if (method.equals("GET") && path.contains("/tasks")){
            System.out.println("Get list.");

            getTask(exchange, id);
        } else if (method.equals("POST") && path.contains("/tasks") && !body.isBlank()){
            System.out.println("Create a task");

            makeTask(exchange, body);
        } else if ((method.equals("PUT") || method.equals("PATCH")) && path.contains("/tasks") && !body.isBlank()){
            System.out.println("Update a task");

            updateTask(exchange, requestId, body);
        } else if (method.equals("DELETE") && path.contains("/tasks")){
            System.out.println("Delete a task");

            deleteTask(exchange, requestId);
        }
    }

    //task 내용을 보여준다
    private void getTask(HttpExchange exchange, Long id) throws IOException {
        Task task = findTask(id);
        String content;

        if(task == null){
            content =  tasksToJSON(tasks);
        }else{
            content =  tasksToJSON(task);
        }

        sendResponse(exchange, content, HttpStatus.OK);
    }

    //body의 내용을 task 객체에 넣어준다.
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    //tasks를 json형태로 보여준다.
    private String tasksToJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        
        return outputStream.toString();
    }

    //task를 등록한다.
    private void makeTask(HttpExchange exchange, String content) throws IOException {
        Task task = toTask(content);
        task.setId(id++);
        tasks.add(task);

        sendResponse(exchange, "Task is Created", HttpStatus.CREATED);
    }

    //요청 id를 가져온다.
    private Long getId(String url){
        Matcher matcher = urlPattern.matcher(url);
        if(matcher.find()){
            return Long.parseLong(matcher.group(1));
        }

        return null;
    }

    //요청 id에 대한 task를 update 한다.
    private void updateTask(HttpExchange exchange, Long id, String content) throws IOException {
        Task task = findTask(id);

        if (task == null){
            sendResponse(exchange, "Task is Not Found", HttpStatus.NOT_FOUND);
        }else {
            task.setTitle(toTask(content).getTitle());
            sendResponse(exchange, "Task is Updated", HttpStatus.CREATED);
        }
    }

    //요청 id에 대한 task를 삭제한다.
    private void deleteTask(HttpExchange exchange, Long id) throws IOException {
        Task task = findTask(id);

        if (task == null){
            sendResponse(exchange, "Task is Not Found", HttpStatus.NOT_FOUND);
        }else {
            tasks.remove(task);
            sendResponse(exchange, "Task is Deleted", HttpStatus.NO_CONTENT);
        }
    }

    private Task findTask(Long id){
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    private void sendResponse(HttpExchange exchange, String content, HttpStatus status) throws IOException {
        exchange.sendResponseHeaders(status.getStatusCode(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
