package com.codesoom.assignment.models;

import com.codesoom.assignment.enums.HttpStatusCode;

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

    public Task addTask(Task newTask) {
        long lastIdx = 0L;
        if (tasks.size() > 0) {
            lastIdx = tasks.get(tasks.size()-1).getId();
        }

        newTask.nextId(lastIdx);

        tasks.add(newTask);

        return getTask(newTask.getTitle());
    }

    public Task getTask(Long taskId) {
        for (Task task : tasks) {
            if (task.equalTaskId(taskId)) {
                return task;
            }
        }

        return null;
    }

    public Task getTask(String title) {
        for (Task task : tasks) {
            if (task.equalTaskTitle(title)) {
                return task;
            }
        }
        return null;
    }

    public void deleteTask(Long taskId) {
        tasks.removeIf(task -> task.equalTaskId(taskId));
    }

}
