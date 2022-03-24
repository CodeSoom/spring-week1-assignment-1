package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private static TaskList taskList;
    private final List<Task> tasks;

    private TaskList() {
        tasks = new ArrayList<>();
    }

    public static TaskList getTaskList() {
        if (taskList == null) {
            taskList = new TaskList();
        }

        return taskList;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task newTask) {
        long lastIdx = tasks.size() == 0 ? 0L : tasks.get(tasks.size()-1).getId();
        newTask.setId(lastIdx + 1);

        tasks.add(newTask);
    }

    public Task getTask(Long taskId) {
        for (Task task : tasks) {
            if (task.equalTaskId(taskId)) {
                return task;
            }
        }

        return null;
    }

    public void deleteTask(Long taskId) {
        tasks.removeIf(task -> task.equalTaskId(taskId));
    }
}
