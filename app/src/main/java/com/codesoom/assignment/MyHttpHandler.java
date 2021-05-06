package com.codesoom.assignment;

import com.codesoom.assignment.models.Todo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(todos);
    }
}
