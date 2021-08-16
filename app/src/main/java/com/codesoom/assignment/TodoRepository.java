package com.codesoom.assignment;

import java.util.ArrayList;
import java.util.List;
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
        if (isNewTask(task)) {
            final Task newTask = new Task(generateId(), task.getTitle());
            store.put(newTask.getId(), newTask);

            return newTask;
        }
        store.put(task.getId(), task);
        return task;
    }

    private Long generateId() {
        sequence += 1;
        return sequence;
    }

    private boolean isNewTask(Task task) {
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

    public List<Task> findAll() {
        return new ArrayList<>(store.values());
    }

    public void deleteById(Long id) {
        if (!store.containsKey(id)) {
            throw new NotFoundEntityException();
        }

        store.remove(id);
    }
}
