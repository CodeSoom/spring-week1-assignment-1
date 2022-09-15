package com.codesoom.assignment.repository;

import com.codesoom.assignment.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepositoryImpl implements TaskRepository {

    private static Map<Long, Task> database = new HashMap<>();
    private static long seq = 0L;
    private static final TaskRepositoryImpl instance = new TaskRepositoryImpl();

    public static TaskRepositoryImpl getInstance() {
        return instance;
    }

    private TaskRepositoryImpl() {}

    @Override
    public Task save(Task task) {
        task.setId(seq++);
        database.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Task findById(Long id) {
        return database.get(id);
    }

    @Override
    public Task updateTask(Task oldTask) {
        Task task = database.get(oldTask.getId());
        task.setTitle(oldTask.getTitle());

        return task;
    }

    @Override
    public boolean deleteTask(Long id) {
        return database.remove(id) != null;
    }
}
