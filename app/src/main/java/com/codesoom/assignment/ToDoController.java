package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.network.HttpResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class ToDoController {

    private final ToDoRepository repository;

    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    public Task getTaskById(Long taskId) throws TaskNotFoundException {
        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isEmpty()) {
            throw new TaskNotFoundException();
        }
        return task.get();
    }

    public void updateTask(Long taskId, String body) throws TaskNotFoundException, JsonProcessingException {
        Task task = getTaskById(taskId);
        repository.updateTask(task, body);
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
