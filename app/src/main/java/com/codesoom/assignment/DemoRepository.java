package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DemoRepository {

    private Long id = 1L;

    private List<Task> tasks = new ArrayList<>();

    public List<Task> readAllTasks() {
        return tasks;
    }

    public Task createTask(String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        tasks.add(task);

        id++;
        return task;
    }

    public Optional<Task> readTaskById(Long param) {
        Task task = null;
        for (Task x : tasks) {
            if (x.getId().equals(param)) {
                task = x;
            }
        }
        return Optional.ofNullable(task);
    }

    public Task updateTask(Task task, String title) {
        Task targetTask = null;
        for (Task x : tasks) {
            if (x.equals(task)) {
                x.setTitle(title);
                targetTask = x;
            }
        }
        return targetTask;
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }
}
