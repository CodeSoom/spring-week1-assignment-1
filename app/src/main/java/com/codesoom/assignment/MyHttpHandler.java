package com.codesoom.assignment;

import com.codesoom.assignment.models.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
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
    public MyHttpHandler() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTodos("Init Todos");
        todos.add(todo);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Todo todo = new Todo();

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("path: " + path);
        System.out.println("getID: " + getID(path));
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(exchange.getRequestMethod().equals("POST")) {
            todo.setId(todos.size() + 1L);
            todo.setTodos(body);
            todos.add(todo);
            System.out.println("todos : " + todos);
        }
    }

    // path의 id값 분리
    Long getID(String path){
        String[] splitPath = path.split("/");
        return Long.parseLong(splitPath[2]);
    }
}
