package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private final List<Map<String,Task>> tasks = new ArrayList<>();
    private final Map<String,Task> taskMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static Long sequence = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method +" "+ path);

        String content = "";
        String id = checkPathGetId(path);
        int httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.getCode();

        // GET /
        if(HttpMethod.GET.getMethod().equals(method) && "/".equals(path)){
            content =  "ToDo List";
            httpStatusCode = HttpStatus.OK.getCode();
        }

        // GET /tasks
        if(HttpMethod.GET.getMethod().equals(method) && "/tasks".equals(path)){
            content =  tasksToJSON();
            httpStatusCode = HttpStatus.OK.getCode();
        }

        // GET /tasks/{id}
        if(HttpMethod.GET.getMethod().equals(method) && ("/tasks/"+id).equals(path)){
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                content =  oneTaskToJSON(task.get());
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // POST /tasks
        if(HttpMethod.POST.getMethod().equals(method) && "/tasks".equals(path)){
            createTask(body);
            content = tasksToJSON();
            httpStatusCode = HttpStatus.CREATED.getCode();
        }

        // PUT,PATCH /tasks/{id}
        if(HttpMethod.PUT.getMethod().equals(method) || HttpMethod.PATCH.equals(method) && ("/tasks/"+id).equals(path)) {
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                Task updateTask = updateTitle(task.get(), body);
                content =  oneTaskToJSON(updateTask);
                httpStatusCode = HttpStatus.OK.getCode();
            }
        }

        // Delete /tasks/{id}
        if(HttpMethod.DELETE.getMethod().equals(method) && ("/tasks/"+id).equals(path)) {
            Optional<Task> task = findId(id);
            httpStatusCode = HttpStatus.NOT_FOUND.getCode();
            if(!task.isEmpty()){
                deleteTodo(id);
                httpStatusCode = HttpStatus.NO_CONTENT.getCode();
            }
        }

        exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);

        OutputStream outputstream = exchange.getResponseBody();
        outputstream.write(content.getBytes());
        outputstream.flush();
        outputstream.close();
    }

    private void createTask(String body) throws JsonProcessingException {
        Task task = jsonToTask(body);
        task.setId(++sequence);
        taskMap.put(task.getId()+"",task);
        tasks.add(taskMap);
    }

    private String checkPathGetId(String path) {
        if(path.indexOf("/tasks/") == 0){
            return path.replace("/tasks/","");
        }
        return "";
    }

    private void deleteTodo(String id) {
        tasks.remove(id);
    }

    private Task updateTitle(Task task, String content) throws JsonProcessingException {
        Task originTask = jsonToTask(content);
        task.setTitle(originTask.getTitle());
        return task;
    }

    private String oneTaskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private Optional findId(String id) {
        Optional<Task> task = Optional.empty();
        Task findTask = taskMap.get(id);
        if(findTask == null){
            return task;
        }
        return task.of(findTask);
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
