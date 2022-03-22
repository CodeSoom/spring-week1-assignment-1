package com.codesoom.assignment.domain;

import com.codesoom.assignment.dto.TaskDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskList {

    private long sequenceId = 0;

    private final List<Task> tasks = new ArrayList<>();

    public TaskList() {
    }

    public Task save(final TaskDto taskDto) {
        Task task = new Task(++sequenceId, taskDto.getTitle());
        tasks.add(task);
        return task;
    }

    public boolean remove(final Task task) {
        return tasks.remove(task);
    }

    public Optional<Task> findTaskById(final Long taskId) {
        Task findTask = tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElse(null);

        if (findTask == null) {
            return Optional.empty();
        }
        return Optional.of(findTask);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
