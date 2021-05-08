package com.codesoom.assignment;

import com.codesoom.assignment.models.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Todo> todos = new ArrayList<>();
    private Long ID = 1L;

    // constructor
//    public MyHttpHandler() {
//        Todo todo = new Todo();
//        todo.setId(1L);
//        todo.setTodo("Init Todos");
//        todos.add(todo);
//    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Todo todo = new Todo();

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        int response = 200;
        String content = "";

        if (method.equals("GET") && body.isBlank() && path.equals("/tasks")) {
//            content = todosToJSON();
            content = todosToJSON();
        }else if(method.equals("GET") && path.contains("/tasks/") && body.isBlank()){
            Long getID = getID(path);
            todo = todos.stream().filter((t) -> t.getId().equals(getID)).findFirst().get();
            content = todosToJSON(todo);
        }
        else if(method.equals("POST") && !body.isBlank() && path.matches("/tasks")) {
            todo = objectMapper.readValue(body, Todo.class);
            todo.setId(ID);
            ID++;
            todos.add(todo);
            content = todosToJSON(todo);
            response = 201;
        } else if (method.equals("PUT") || method.equals("PATCH") && path.contains("/tasks/") && !body.isBlank()){
            try {
                Long getID = getID(path);
                todos.stream().filter(t -> t.getId().equals(getID)).findFirst().get();
                content = updateTodo(todo, bodyToTask(body).getTodo());
                response = 200;
            }catch (Exception e){
                content = "Something went wrong";
                response = 404;
            }
        }
        exchange.sendResponseHeaders(response, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
    private String todosToJSON() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, todos);
        return outputStream.toString();
    }
    private String todosToJSON(Todo todo) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, todo);
        return outputStream.toString();
    }

    private Todo bodyToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Todo.class);
    }

    // path의 id값 분리
    Long getID(String path){
        String[] splitPath = path.split("/");
        return Long.parseLong(splitPath[2]);
    }

    String updateTodo(Todo todo, String body) throws IOException {
        todo.setTodo(body);
        return todosToJSON(todo);
    }


}
