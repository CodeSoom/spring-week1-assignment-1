package com.codesoom.assignment.repository;

import com.codesoom.assignment.dto.Task;
import com.codesoom.assignment.exception.DoesNotExistException;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private final List<Task> tasks;

    public TaskRepository() {
        this.tasks = new ArrayList<>();
    }

    public Task findById(Long id) throws DoesNotExistException {
        return this.tasks.stream()
                .filter((task) -> task.getId().equals(id))
                .findFirst()
                .orElseThrow(DoesNotExistException::new);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Boolean deleteTask(Long id) {
        return this.tasks.removeIf((task) -> task.getId().equals(id));
    }

    public List<Task> findAll() {
        return this.tasks;
    }

    public Task updateTask(Long id, String title) throws DoesNotExistException {
        if (this.tasks.removeIf((task) -> task.getId().equals(id))) {
            Task task = new Task(id, title);
            this.tasks.add(task);
            return task;
        } else {
            throw new DoesNotExistException();
        }
    }
}
