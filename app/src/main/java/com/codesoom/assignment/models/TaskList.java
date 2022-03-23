package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private static TaskList taskList;
    private List<Task> tasks;

    private TaskList() {
        tasks = new ArrayList<>();
        dummyDatas();
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

    private void dummyDatas() {
        tasks.add(new Task(1L, "codesoom lecture complete 1"));
        tasks.add(new Task(2L, "codesoom lecture complete 2"));
        tasks.add(new Task(3L, "codesoom lecture complete 3"));
        tasks.add(new Task(4L, "codesoom lecture complete 4"));
    }

}
