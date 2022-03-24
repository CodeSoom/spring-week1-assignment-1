package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TodoRepository {

    private List<Task> tasks = new ArrayList<>();

    private Long getNewId() {
        if(tasks.isEmpty()) return 1L;
        Task task = tasks.stream()
                .sorted(Comparator.comparing(Task::getId).reversed())
                .findFirst()
                .get();
        return task.getId() + 1L;
    }

    public List<Task> findAllTasks() {
        return tasks;
    }

    public Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }

    public Task save(Task task) {
        if(task.getId() == null) task.setId(getNewId());
        tasks.add(task);
        return task;
    }
}
