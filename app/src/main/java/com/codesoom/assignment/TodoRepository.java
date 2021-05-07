package com.codesoom.assignment;

import com.codesoom.assignment.models.Todo;

import java.util.HashMap;
import java.util.Map;


// 입력 값을 map 형태로 저장
public class TodoRepository {
    private final Map<Long, Todo> todo;

    public TodoRepository(){
        this.todo = new HashMap<>();
    }
    // GET By ID
    public Todo findById(Long id) {
        return this.todo.get(id);
    }

    // POST
    public void addTodo(Long id, Todo todo) {
        this.todo.put(id, todo);
    }

    // PUT
    public void updateTodo(Long id, Todo todo) {
        this.todo.put(id, todo);
    }

    // Delete
    public void deleteTodo(Long id) {
        todo.remove(id);
    }

}
