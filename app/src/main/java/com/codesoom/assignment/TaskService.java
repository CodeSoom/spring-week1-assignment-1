package com.codesoom.assignment;
import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class TaskManager {
    private Long nextId = 1L;
    private List<Task> tasks = new ArrayList<>();

    public Task createTask(String title) {
        Task task = new Task(nextId++, title);
        tasks.add(task);
        return task;
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task getTaskById(Long id) throws NoSuchElementException {
        return tasks.stream()
                .filter(task->task.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("Not Found Task"));
    }

    public void removeTask(Long id) throws NoSuchElementException {
        if(!checkTask(id)) {
            throw new NoSuchElementException("Not Found Task");
        }

        tasks = tasks.stream()
                .filter(task->!(task.getId().equals(id)))
                .collect(Collectors.toList());
    }

    public void updateTask(Long id, String title) {
        Task resultTask = getTaskById(id);
        resultTask.setTitle(title);
    }

    private boolean checkTask(Long id) {
        long countResult = tasks.stream()
                            .filter(task->task.getId().equals(id))
                            .count();

        return countResult > 0;
    }
}
