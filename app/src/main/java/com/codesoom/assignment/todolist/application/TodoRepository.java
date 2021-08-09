package com.codesoom.assignment.todolist.application;

import com.codesoom.assignment.todolist.domain.Task;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TodoRepository {
    private static final TodoRepository instance = new TodoRepository();
    private static final Map<Long, Task> store = new ConcurrentHashMap<>();
    private static Long sequence = 0L;

    private TodoRepository() {}

    public static TodoRepository getInstance() {
        return instance;
    }

    public Task save(Task task) {
        if (isNew(task)) {
            final Task newTask = new Task(++sequence, task.getTitle());
            store.put(newTask.getId(), newTask);

            return newTask;
        }
        store.put(task.getId(), task);
        return task;
    }

    private boolean isNew(Task task) {
        return task.getId() == null || !store.containsKey(task.getId());
    }

    public void delete(Task task) {
        if (store.containsValue(task)) {
            store.remove(task.getId());
        }
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
