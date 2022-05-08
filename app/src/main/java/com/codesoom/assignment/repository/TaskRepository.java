package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskRepository {
    private static Map<Long, Task> store = new HashMap<>();
    private static Long sequence = 1L;

    private static final TaskRepository instance = new TaskRepository();

    public static TaskRepository getInstance() {
        return instance;
    }

    private TaskRepository() {}

    public Task save(Task task) {
        task.setId(sequence);
        store.put(task.getId(), task);

        sequence++;
        return task;
    }

    public Task findById(Long id) {
        return store.get(id);
    }

    public List<Task> findAll() {
//        return new ArrayList<>(store.values());
        return new ArrayList<>(store.values().stream()
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList()));
    }

    public void delete(Long id) {
        store.remove(id);
    }

    public Task update(Long id, Task newTask) {
        newTask.setId(id);
        store.put(newTask.getId(), newTask);
        return newTask;
    }

}
