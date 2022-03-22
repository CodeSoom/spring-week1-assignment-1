package com.codesoom.assignment.domain.repository;

import com.codesoom.assignment.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface Repository {
    Task save(Task task);
    Optional<Task> findById(String taskId);
    Task getById(String taskId);
    List<Task> allTasks();
}
