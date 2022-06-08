package com.codesoom.assignment.services;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private final List<Task> registeredTasks = new ArrayList<>();

    public Task show(Long id) {
        for (Task task: registeredTasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }

        return null;
    }

    public List<Task> showAll() {
        return this.registeredTasks;
    }

    public Task register(Task newTask) {
        Long newId = nextId();
        newTask.setId(newId);
        this.registeredTasks.add(newTask);
        return newTask;
    }

    public Task modify(Task newTask) {
        for (Task task: registeredTasks) {
            if (task.getId().equals(newTask.getId())) {
                registeredTasks.remove(task);
                registeredTasks.add(newTask);
                return newTask;
            }
        }

        return null;
    }

    public Task delete(Long id) {
        for (Task task: registeredTasks) {
            if (task.getId().equals(id)) {
                registeredTasks.remove(task);
                return task;
            }
        }

        return null;
    }

    private Long nextId() {
        if (registeredTasks.isEmpty()) {
            return 1L;
        }

        Task lastTask = registeredTasks.get(registeredTasks.size() - 1);
        return lastTask.getId() + 1;
    }
}
