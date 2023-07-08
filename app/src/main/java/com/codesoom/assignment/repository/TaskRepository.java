package com.codesoom.assignment.repository;

import com.codesoom.assignment.model.Task;

import java.util.List;
import java.util.NoSuchElementException;

public interface TaskRepository {

    void save(Task task);

    List<Task> findAll();

    Task findById(Long id) throws NoSuchElementException;

    void delete(Task task);

    Task update(Task task, Task updateTask);


}
