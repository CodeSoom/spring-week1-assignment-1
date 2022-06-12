package com.codesoom.assignment.services;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    
    private final Map<Long, String> registeredTasks = new HashMap();
    private Long lastId = 0L;

    public Task show(Long id) {
        String title = registeredTasks.get(id);
        if (title == null) return null;

        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    public List<Task> showAll() {
        List<Task> allTasks = new ArrayList<Task>();

        for (Long id: registeredTasks.keySet()) {
            Task task = new Task();
            task.setId(id);
            task.setTitle(registeredTasks.get(id));
            allTasks.add(task);
        }

        return allTasks;
    }

    public Task register(String title) {
        Long newId = nextId();
        this.registeredTasks.put(newId, title);

        Task task = new Task();
        task.setId(newId);
        task.setTitle(title);
        return task;
    }

    public Task modify(Task newTask) {
        if (!this.registeredTasks.containsKey(newTask.getId())) {
            return null;
        }

        this.registeredTasks.put(newTask.getId(), newTask.getTitle());
        return newTask;
    }

    public Task delete(Long id) {
        if (!this.registeredTasks.containsKey(id)) {
            return null;
        }

        String title = this.registeredTasks.remove(id);
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        return task;
    }

    private Long nextId() {
        lastId += 1L;

        return lastId;
    }
}
