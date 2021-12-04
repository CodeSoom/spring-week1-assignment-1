package com.codesoom.assignment.task.service;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.repository.TaskRepository;

import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService() {
        this.taskRepository = new TaskRepository();
    }

    public Task findByTaskId(long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findALL() {
        return taskRepository.findAll();
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Task source) {
        task.setTitle(source.getTitle());

        return task;
    }

    public void removeTask(Task task) {
        taskRepository.delete(task);
    }
}
