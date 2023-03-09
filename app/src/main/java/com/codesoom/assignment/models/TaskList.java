package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList {
    private final List<Task> taskList = new ArrayList<>();

    public void add(Task task) {
        int listSize = this.taskList.size();
        setTaskId(task, listSize);
        taskList.add(task);
    }

    public List<Task> getItems() {
        return this.taskList;
    }

    private void setTaskId(Task task, int listSize) {
        if (listSize == 0) {
            task.setId(1);
            return;
        }

        Task laskTask = this.taskList.get(listSize - 1);
        task.setId(laskTask.getId() + 1);
    }

    public int size() {
        return this.taskList.size();
    }

    public Task get(int i) {
        List<Task> collect = taskList.stream()
                .filter(task -> task.isTaskId(i))
                .collect(Collectors.toList());
        if (collect.size() == 0) {
            return null;
        }
        return collect.get(0);
    }

    public boolean delete(int requestTaskId) {
        Task task = get(requestTaskId);
        if (task == null) {
            return false;
        }
        return this.taskList.remove(task);
    }

    public Task updateTask(int requestTaskId, Task requestTask) {
        Task task = this.taskList.get(requestTaskId);
        if (task == null) {
            throw new NullPointerException();
        }
        task.updateTitle(requestTask.getTitle());
        return task;
    }
}
