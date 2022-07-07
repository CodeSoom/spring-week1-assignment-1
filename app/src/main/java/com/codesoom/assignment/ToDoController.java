package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;

import java.util.Optional;

public class ToDoController {

    private final ToDoRepository repository;

    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    public void deleteTask(Long taskId) throws TaskNotFoundException {
        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.deleteTask(exitedTask);
        } else {
            throw new TaskNotFoundException();
        }
    }
}
