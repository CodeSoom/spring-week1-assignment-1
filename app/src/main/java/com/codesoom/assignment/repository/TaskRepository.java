package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {

    private static final TaskRepository instance = new TaskRepository();
    private static final Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    private TaskRepository() {

    }

    public static TaskRepository getInstance() {
        return instance;
    }

    public Task findById(Long id) {
        return taskMap.get(id);
    }

    public void addNewTask(Task task) {
        task.allocateId();
        taskMap.put(task.getId(), task);
    }

    public Task editTaskById(Long id, Task newTask) {
        final Task originalTask = taskMap.get(id);
        if (originalTask == null)
            return null;

        originalTask.setTitle(newTask.getTitle());
        return originalTask;
    }

    public Task deleteById(Long id) {
        return taskMap.remove(id);
    }

    public Collection<Task> getAllTasks() {
        return taskMap.values();
    }
}
