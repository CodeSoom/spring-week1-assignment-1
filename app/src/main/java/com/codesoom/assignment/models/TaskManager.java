package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private Long idSeq = 0L;

    private Long generateId() {
        return idSeq++;
    }

    public void insertTask(Task task) {
        tasks.add(new Task(generateId(), task.getTitle()));
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public void updateTask(Task task) {
        tasks.get(tasks.indexOf(task)).setTitle(task.getTitle());
    }

    public List<Task> findTaskAll() {
        return tasks;
    }

    public Task findTask(Task findTask) {
        for (Task task : tasks) {
            if (task.getId().equals(findTask.getId())) {
                return task;
            }
        }
        return new Task();
    }
}
