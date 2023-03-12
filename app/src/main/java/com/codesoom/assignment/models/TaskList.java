package com.codesoom.assignment.models;

import com.codesoom.assignment.config.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> taskList = new ArrayList<>();
    private int newId = 0;

    public void add(Task task) {
        task.setId(generatedId());
        taskList.add(task);
    }

    public List<Task> getItems() {
        return this.taskList;
    }

    private int generatedId() {
        return newId += 1;
    }

    public int size() {
        return this.taskList.size();
    }

    public Task get(int idx) throws TaskNotFoundException {
        return taskList.stream()
                .filter(task -> task.isTaskId(idx))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException("tasks not found"));
    }

    public boolean delete(int requestTaskId) throws TaskNotFoundException {
        return this.taskList.remove(get(requestTaskId));
    }

    public void updateTask(int requestTaskId, Task requestTask) throws TaskNotFoundException {
        Task task = get(requestTaskId);
        task.updateTitle(requestTask.getTitle());
    }
}
