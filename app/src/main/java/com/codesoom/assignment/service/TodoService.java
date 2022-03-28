package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

public class TodoService {

    private TodoRepository todoRepository = new TodoRepository();

    public List<Task> findAllTasks() {
        return todoRepository.findAllTasks();
    }

    public Optional<Task> findTaskById(Long id) {
        return todoRepository.findTaskById(id);
    }

    public Task saveTask(Task task) {
        return todoRepository.save(task);
    }

    public Task updateTask(Task task, Task requestTaskInfo) {

        task.setTitle(requestTaskInfo.getTitle());

        return task;
    }

    public void deleteTask(Task task) {

        todoRepository.deleteTask(task);
    }

}
