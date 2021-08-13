package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class TaskManager {
    private Long nextId = 1L;
    private List<Task> tasks = new ArrayList<>();

    public void create(String title) {
        Task newTask = new Task(this.nextId++, title);
        this.tasks.add(newTask);
    }

    public List<Task> getAll() {
        return this.tasks;
    }

    public Task getOne(Long id) throws NoSuchElementException {
        return this.tasks.stream()
                .filter(task->task.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new NoSuchElementException("Not Found Task"));
    }

    public Task getLast() throws NoSuchElementException {
        return getOne(nextId - 1);
    }

    public void remove(Long id) throws NoSuchElementException {
        if(!exist(id)) {
            throw new NoSuchElementException("Not Found Task");
        }

        this.tasks = this.tasks.stream()
                .filter(task->!(task.getId().equals(id)))
                .collect(Collectors.toList());
    }

    public void update(Long id, String title) throws NoSuchElementException {
        Task updateTask = getOne(id);
        updateTask.setTitle(title);
    }

    private boolean exist(Long id) {
        long countResult = tasks.stream()
                .filter(task->task.getId().equals(id))
                .count();

        return countResult > 0;
    }


}
