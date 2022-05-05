package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.*;

public class TaskRepository {
    private Long sequence = 1L;
    private Map<Long, Task> taskMap = new HashMap<>();

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    public Task save(Task task) {
        task.setId(sequence);
        taskMap.put(task.getId(), task);

        incrementSequence();
        return task;
    }

    public void delete(Long id) {
        taskMap.remove(id);
    }

    private void incrementSequence() {
        this.sequence += 1;
    }

}
