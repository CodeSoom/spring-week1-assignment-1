package com.codesoom.assignment.controller;

import com.codesoom.assignment.models.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoHttpController {
    private List<Todo> todoList;
    private Integer id;
    public TodoHttpController() {
        this.todoList = new ArrayList<>();
        this.id = 0;
    }

    public List<Todo> getTodos() {
        return this.todoList;
    }

    public Todo getTodos(String id) {
        return this.todoList.stream()
                .filter(t -> t.getId().equals(Integer.parseInt(id)))
                .findFirst()
                .get();
    }

    public boolean isEmpty() {
        return this.todoList.size() == 0;
    }

    public Todo insert(Todo readValue) {
        readValue.setId(this.id++);
        this.todoList.add(readValue);
        return readValue;
    }

    public Todo update(Todo body) {
        for (Todo todo : todoList) {
            if (todo.getId().equals(body.getId())) {
                todo.setTitle(body.getTitle());
                return todo;
            }
        }
        return body;
    }

    public boolean isExist(String id) {
        return todoList.stream().filter(t -> t.getId() == Integer.parseInt(id)).count() != 0;
    }

    public void delete(String id) {
        todoList.removeIf(t -> t.getId() == Integer.parseInt(id));
    }
}
