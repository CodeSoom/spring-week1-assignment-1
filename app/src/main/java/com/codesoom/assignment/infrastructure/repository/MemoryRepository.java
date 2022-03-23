package com.codesoom.assignment.infrastructure.repository;

import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.repository.Repository;
import com.codesoom.assignment.domain.todo.Todo;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemoryRepository implements Repository {
    private static Map<String, Todo> store = new ConcurrentHashMap<>();

    @Override
    public Todo save(Todo todo) {
        store.put(todo.getTodoId(), todo);
        return todo;
    }


    @Override
    public Optional<Todo> findById(String todoId) {
        return Optional.ofNullable(store.get(todoId));
    }

    @Override
    public Todo getById(String todoId) {
        Todo todo = findById(todoId).orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_TASK.getErrorMsg()));
        return todo;
    }


    @Override
    public List<Todo> allTodos() {
        return store.values().stream().collect(Collectors.toList());
    }
}
