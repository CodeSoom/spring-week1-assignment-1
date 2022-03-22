package com.codesoom.assignment.infrastructure.task;

import com.codesoom.assignment.domain.repository.Repository;
import com.codesoom.assignment.domain.task.Task;
import com.codesoom.assignment.domain.task.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {
    private final Repository repository;

    public TaskServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Task save(Task task) {
        return repository.save(task);
    }

    @Override
    public Task update(String taskId, String title) {
        Task task = getById(taskId);
        task.updateTitle(title);
        return repository.save(task);
    }

    @Override
    public Task findById(String taskId) {
        return null;
    }

    @Override
    public Task getById(String taskId) {
        return repository.getById(taskId);
    }

    @Override
    public List<Task> allTasks() {
        return repository.allTasks();
    }
}
