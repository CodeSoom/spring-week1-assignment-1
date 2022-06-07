package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private final List<Task> registeredTasks = new ArrayList<>();

    public Task show(Long id) {
        return this.registeredTasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Task> showAll() {
        return this.registeredTasks;
    }

    public Task register(Task task) {
        this.registeredTasks.add(task);
        return task;
    }

    public Task modify(Long id, Task task) {
        return null;
    }

    public Task delete(Long id) {
        return null;
    }
}
