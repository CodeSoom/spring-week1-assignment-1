package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoRepository {

    private List<Task> tasks = new ArrayList<>();

    public List<Task> findAllTasks() {
        return tasks;
    }

    public Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();
    }
}
