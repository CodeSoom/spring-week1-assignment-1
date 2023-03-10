package com.codesoom.assignment.models;

import com.codesoom.assignment.config.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskList {
    private final List<Task> taskList = new ArrayList<>();

    public void add(Task task) {
        setTaskId(task);
        taskList.add(task);
    }

    public List<Task> getItems() {
        return this.taskList;
    }

    private void setTaskId(Task task) {
        if (size() == 0) {
            task.setId(1);
            return;
        }

        Task laskTask = this.taskList.get(size() - 1);
        task.setId(laskTask.getId() + 1);
    }

    public int size() {
        return this.taskList.size();
    }

    public Task get(int idx) throws TaskNotFoundException {
        Optional<Task> taskOptional = taskList.stream()
                .filter(task -> task.isTaskId(idx))
                .findFirst();

        if (taskOptional.isEmpty()) {
            throw new TaskNotFoundException("taskEmpty");
        }

        return taskOptional.get();
    }

    public boolean delete(int requestTaskId) throws TaskNotFoundException {
        return this.taskList.remove(get(requestTaskId));
    }

    public void updateTask(int requestTaskId, Task requestTask) throws TaskNotFoundException {
        Task task = get(requestTaskId);
        task.updateTitle(requestTask.getTitle());
    }
}
