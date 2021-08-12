package com.codesoom.assignment;
import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class TaskDatabase {
    private Long nextId = 1L;
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(Long id) throws NoSuchElementException {
        return tasks.stream()
                .filter(task->task.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("Not Found Task"));
    }

    public void removeTaskById(Long id) throws NoSuchElementException {
        tasks = tasks.stream()
                .filter(task->!(task.getId().equals(id)))
                .collect(Collectors.toList());
    }

    
}
