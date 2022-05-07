package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> TASKS = new HashMap<>();

    public static List<Task> findAll() {
        return new ArrayList<>(TASKS.values());
    }

    public static Task insert(Task task) throws ClassNotFoundException, AlreadyExistsIdException {
        synchronized (Class.forName("com.codesoom.assignment.models.TaskManager")) {
            if (TASKS.get(task.id()) != null) {
                throw new AlreadyExistsIdException();
            }
            index++;
            Task newTask = new Task(index, task.title());
            TASKS.put(index, newTask);
            return newTask;
        }
    }

    public static Task find(Long id) {
        return TASKS.get(id);
    }

    public static Task modify(Long id, Task task) {
        TASKS.replace(id, task);
        Task newTask = new Task(id, task.title());
        return newTask;
    }

    public static void delete(Long id) {
        TASKS.remove(id);
        return;
    }
}
