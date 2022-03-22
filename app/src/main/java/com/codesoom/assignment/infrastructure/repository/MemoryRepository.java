package com.codesoom.assignment.infrastructure.repository;

import com.codesoom.assignment.common.response.ErrorCode;
import com.codesoom.assignment.domain.repository.Repository;
import com.codesoom.assignment.domain.task.Task;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemoryRepository implements Repository {
    private static Map<String, Task> store = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        store.put(task.getTaskId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String taskId) {
        return Optional.ofNullable(store.get(taskId));
    }

    @Override
    public Task getById(String taskId) {
        Task task = findById(taskId).orElseThrow(() -> new NoSuchElementException(ErrorCode.NO_TASK.getErrorMsg()));
        return task;
    }


    @Override
    public List<Task> allTasks() {
        return store.values().stream().collect(Collectors.toList());
    }
}
