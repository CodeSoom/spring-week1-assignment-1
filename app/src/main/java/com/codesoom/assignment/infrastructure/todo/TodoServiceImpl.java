package com.codesoom.assignment.infrastructure.todo;

import com.codesoom.assignment.domain.repository.Repository;
import com.codesoom.assignment.domain.todo.Todo;
import com.codesoom.assignment.domain.todo.TodoService;

import java.util.List;

public class TodoServiceImpl implements TodoService {
    private final Repository repository;

    public TodoServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Todo save(Todo todo) {
        return repository.save(todo);
    }

    @Override
    public Todo update(String todoId, String title) {
        Todo todo = getById(todoId);
        todo.updateTitle(title);
        return repository.save(todo);
    }

    @Override
    public Todo findById(String todoId) {
        return null;
    }

    @Override
    public Todo getById(String todoId) {
        return repository.getById(todoId);
    }

    @Override
    public List<Todo> allTasks() {
        return repository.allTodos();
    }
}
