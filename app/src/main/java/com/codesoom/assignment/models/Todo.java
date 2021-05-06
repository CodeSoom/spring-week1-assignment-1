package com.codesoom.assignment.models;

public class Todo {

    private Long id;
    private String todos;


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public String getTodos() {
        return todos;
    }
    public void setTodos(String todos) {
        this.todos = todos;
    }
}
