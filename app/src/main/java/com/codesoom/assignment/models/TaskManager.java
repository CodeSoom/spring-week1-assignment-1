package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> TASKS = new HashMap<>();

    public static List<Task> findALl() {
        return new ArrayList<>(TASKS.values());
    }

    public static Task insert(Task task) throws ClassNotFoundException {
        synchronized (Class.forName("com.codesoom.assignment.models.TaskManager")) {
            index++;
            Task newTask = new Task(index, task.getTitle());
            TASKS.put(index, task);
            return newTask;
        }
    }

}
