package com.codesoom.assignment.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> tasks = new HashMap<>();

    public static List<Task> find() {
        return new ArrayList<>(tasks.values());
    }

    public static Task find(long id) {
        return tasks.get(id);
    }

    public static void insert(Task task) throws Exception {
        if (tasks.get(task.id()) != null) {
            throw new Exception("id " + task.id() + " is already exists");
        }
        tasks.put(task.id(), task);
    }

    public static Task insert(String title) {
        index += 1;
        if (tasks.get(index) == null) {
            Task task = new Task(index, title);
            tasks.put(index, task);
            return task;
        }
        return insert(title);
    }

    public static void modify(Task task) throws Exception {
        if (tasks.get(task.id()) == null) {
            throw new Exception("not exist task id");
        }
        tasks.replace(task.id(), task);
    }

    public static void delete(long id) throws Exception {
        if (tasks.get(id) == null) {
            throw new Exception("not exist task id");
        }
        tasks.remove(id);
    }
}
