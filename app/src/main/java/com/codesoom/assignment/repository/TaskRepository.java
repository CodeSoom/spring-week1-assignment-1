package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {

    private static final TaskRepository instance = new TaskRepository();
    private static final IdGenerator idGenerator = IdGenerator.getInstance();
    private static final Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    private TaskRepository() {
    }

    public static TaskRepository getInstance() {
        return instance;
    }

    public Task findById(Long id) {
        return taskMap.get(id);
    }

    public Task addNewTask(String title) {
        Long id = idGenerator.allocateId();
        Task newTask = new Task(id, title, LocalDateTime.now());
        taskMap.put(id, newTask);

        return newTask;
    }

    public Task editTaskById(Long id, String newTitle) {
        final Task originalTask = taskMap.get(id);
        if (originalTask == null) {
            return null;
        }

        originalTask.changeTitle(newTitle);
        return originalTask;
    }

    public Task deleteById(Long id) {
        return taskMap.remove(id);
    }

    public Collection<Task> getAllTasks() {
        return taskMap.values();
    }
}
