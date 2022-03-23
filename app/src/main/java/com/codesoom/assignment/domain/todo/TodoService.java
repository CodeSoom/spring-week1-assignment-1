package com.codesoom.assignment.domain.todo;

import java.util.List;

public interface TodoService {

    Todo save(Todo todo);
    Todo update(String todoId, String title);
    Todo findById(String id);
    Todo getById(String id);
    List<Todo> allTasks();
}
