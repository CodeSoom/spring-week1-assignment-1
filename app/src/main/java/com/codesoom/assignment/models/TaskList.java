package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

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
        return this.taskList.get(i);
    }
}
