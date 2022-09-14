package com.codesoom.assignment.repository;

import com.codesoom.assignment.model.Task;

import java.util.List;

public interface TaskRepository {
    Task save(Task task);

    List<Task> findAll();

    Task findById(Long id);

    Task updateTask(Task oldTask);

    boolean deleteTask(Long id);
}
