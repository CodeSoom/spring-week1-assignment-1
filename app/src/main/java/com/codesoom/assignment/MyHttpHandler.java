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
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        System.out.println("body: " + body); // 객체 형태로 나오지만 사실은 String
        System.out.println("body Type: " + body.getClass().getTypeName());

        // 1차 시도 실패 -> 주소 값이 todos 에 저장됨
        TodoRepository todoRepository = new TodoRepository();

        if(exchange.getRequestMethod().equals("POST")) {
            todo.setId(todos.size() + 1L);
            todo.setTodos(body);
            todos.add(todo);
            System.out.println("todos : " + todos);
        }
    }
}
