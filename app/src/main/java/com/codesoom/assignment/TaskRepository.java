package com.codesoom.assignment;

import java.util.*;
import java.util.stream.Collectors;

public class TaskRepository {
    static private Long maxId = 1L;
    static private Map<Long, Task> tasks;

    TaskRepository() {
        tasks = new HashMap<>();
    }

    public Task taskBy(Long id) {
        Task foundTask = tasks.get(id);

        if (foundTask == null) {
            throw new NoSuchElementException("taskId(" + id + ")에 해당하는 Task를 저장소에서 찾을 수 없습니다");
        }

        return foundTask;
    }

    public List<Task> tasksAll() {
        return tasks.values().stream()
                .sorted(Comparator.comparing(Task::getId)) //.sorted((Task t1, Task t2) -> t1.getId().compareTo(t2.getId()))
                .collect(Collectors.toList());
    }

    public Task save(Task task) {
        long newId = generateTaskId();
        task.setId(newId);
        tasks.put(newId, task);

        return task;
    }

    public void delete(Long id) {
        final Task task = taskBy(id);
        tasks.remove(task.getId());
    }

    public Task update(Long oldTaskId, Task newTask) {
        final Task oldTask = taskBy(oldTaskId);
        newTask.setId(oldTask.getId());
        tasks.replace(oldTask.getId(), newTask);
        return newTask;
    }

    private Long generateTaskId() {
        Long generatedId = maxId;
        maxId++;
        return generatedId;
    }
}
