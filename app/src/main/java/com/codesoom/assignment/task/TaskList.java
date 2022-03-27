package com.codesoom.assignment.task;

import com.codesoom.assignment.task.exception.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;

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

    public void remove(final Task task) {
        tasks.remove(task);
    }

    public Task findTaskById(final Long taskId) {
        return tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(TaskNotFoundException::new);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
