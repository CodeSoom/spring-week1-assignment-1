package com.codesoom.assignment.todolist.application;

import com.codesoom.assignment.todolist.domain.Task;
import com.codesoom.assignment.todolist.domain.TodoRepository;
import com.codesoom.assignment.todolist.exceptions.NotFoundEntityException;

import java.util.List;

public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Task save(Task task) {
        return todoRepository.save(task);
    }

    public Task update(Long id, Task task) {
        todoRepository.findById(id)
                .ifPresent(findTask-> findTask.updateTitle(task.getTitle()));

        return findById(id);
    }

    public Task findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(NotFoundEntityException::new);
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public List<Task> findAll() {
        return todoRepository.findAll();
    }
}
