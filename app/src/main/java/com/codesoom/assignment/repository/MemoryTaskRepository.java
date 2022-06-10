package com.codesoom.assignment.repository;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryTaskRepository {
    private List<Task> tasks = new ArrayList<>();
    private Long sequence = 0L;

    public synchronized Task  save(Task task) {
        task.setId(sequence++);
        tasks.add(task);
        return task;
    }

    public Optional<Task> findById(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findAny();

    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> update(Long taskId, String newTitle) {
        Optional<Task> changeTask = tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findAny();
        if (!changeTask.isPresent()) {
            return changeTask;
        }
        changeTask.get().setTitle(newTitle);
        return changeTask;
    }

    public void delete(Task deleteTask) {
        tasks.remove(deleteTask);
    }

}
