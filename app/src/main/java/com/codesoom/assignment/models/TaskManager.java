package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private Long idSeq = 0L;

    private Long generateId() {
        return idSeq++;
    }

    public Long insertTask(Task task) {
        Task newTask = new Task(generateId(), task.getTitle());
        tasks.add(newTask);
        return newTask.getId();
    }

    public void deleteTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (task.getId().equals(tasks.get(i).getId())) {
                tasks.remove(i);
                break;
            }
        }

    }

    public Long updateTask(Task task) {
        tasks.stream().forEach(target -> {
            if (target.getId().equals(task.getId())) {
                target.setTitle(task.getTitle());
            }
        });
        return task.getId();
    }

    public List<Task> findTaskAll() {
        return tasks;
    }

    public Task findTaskById(Long id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return new Task();
    }
}
