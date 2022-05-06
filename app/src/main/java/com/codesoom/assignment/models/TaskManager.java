package com.codesoom.assignment.models;

import com.codesoom.assignment.errors.AlreadyExistsIdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static long index = 0;
    private static final HashMap<Long, Task> TASKS = new HashMap<>();

    public static List<Task> findALl() {
        return new ArrayList<>(TASKS.values());
    }

    public static Task insert(Task task) throws ClassNotFoundException, AlreadyExistsIdException {
        synchronized (Class.forName("com.codesoom.assignment.models.TaskManager")) {
            if (TASKS.get(task.id()) != null) {
                throw new AlreadyExistsIdException();
            }
            index++;
            Task newTask = new Task(index, task.title());
            TASKS.put(index, task);
            return newTask;
        }
    }

    public static Task find(Long id) {
        return TASKS.get(id);
    }

}
