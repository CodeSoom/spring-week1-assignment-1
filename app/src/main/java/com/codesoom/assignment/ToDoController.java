package com.codesoom.assignment;

import com.codesoom.assignment.exception.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.network.HttpResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ToDoController {

    private final ToDoRepository repository;
    private final AtomicLong lastId = new AtomicLong(1L);
    private final TaskMapper mapper = new TaskMapper();

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

    public List<Task> getTasks() {
        return repository.getTasks();
    }

    public Task addTask(String body) throws JsonProcessingException {
        Task task = mapper.stringToTask(body);
        task.setId(lastId.getAndIncrement());
        repository.addTask(task);
        return task;
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
