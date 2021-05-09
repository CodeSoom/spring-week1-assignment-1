package com.codesoom.assignment.repository;

import com.codesoom.assignment.dto.Task;
import com.codesoom.assignment.exception.DoesNotExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private final Map<Long, Task> tasks;

    public TaskRepository() {
        this.tasks = new HashMap<>();
    }

    public Task findById(Long id) throws DoesNotExistException {
        Task task = this.tasks.get(id);
        if (task == null) throw new DoesNotExistException();
        return task;
    }

    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public void deleteTask(Long id) throws DoesNotExistException {
        Task task = this.tasks.remove(id);
        if (task == null) throw new DoesNotExistException();
    }

    public List<Task> findAll() {
        List<Task> ret = new ArrayList<>();
        for(Long key: this.tasks.keySet()) {
            ret.add(this.tasks.get(key));
        }
        return ret;
    }

    public Task updateTask(Long id, String title) throws DoesNotExistException {
        Task task = this.tasks.get(id);
        if (task == null) throw new DoesNotExistException();
        task.setTitle(title);
        return this.tasks.get(id);
    }
}
