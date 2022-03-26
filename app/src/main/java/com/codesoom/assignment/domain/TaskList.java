package com.codesoom.assignment.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskList {

    private long sequenceId = 0;

    private final List<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public Task save(final Task task) {
        sequenceId = sequenceId + 1;
        task.setId(sequenceId);

        tasks.add(task);
        return task;
    }

    public boolean remove(final Task task) {
        return tasks.remove(task);
    }

    public Optional<Task> findTaskById(final Long taskId) {
        return tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
