package com.codesoom.assignment.repository;

import com.codesoom.assignment.utils.IdGenerator;
import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {

    private Map<Long, Task> taskMap = new HashMap<>();
    private IdGenerator idGenerator = new IdGenerator();

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>(taskMap.values());
        return tasks;
    }

    public Task save(Task task) {
        Long id = idGenerator.getId();
        task.setId(id);
        taskMap.put(id, task);

        return task;
    }

    public Task findOne(Long id) {
        return taskMap.get(id);
    }

    public Task update(Long id, Task newTask) {
        newTask.setId(id);
        taskMap.put(id, newTask);

        return newTask;
    }

    public void delete(Long id) {
        taskMap.remove(id);
    }

}
