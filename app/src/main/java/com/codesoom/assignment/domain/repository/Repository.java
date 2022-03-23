package com.codesoom.assignment.domain.repository;

import com.codesoom.assignment.domain.todo.Todo;

import java.util.List;
import java.util.Optional;

public interface Repository {

    Todo save(Todo todoId);
    Optional<Todo> findById(String todoId);
    Todo getById(String todoId);
    List<Todo> allTodos();
}
