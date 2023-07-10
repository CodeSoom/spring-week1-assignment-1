package com.codesoom.assignment.repository.impl;

import com.codesoom.assignment.model.Task;
import com.codesoom.assignment.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TaskListRepositoryImpl implements TaskRepository {
    private static TaskListRepositoryImpl instance;
    private ArrayList<Task> taskList = new ArrayList<>();
    private Long taskId = 1L;

    private TaskListRepositoryImpl() {
    }

    public static TaskListRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new TaskListRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void save(Task task) {
        task.setId(taskId);
        taskList.add(task);
        taskId++;
    }

    @Override
    public List<Task> findAll() {
        return taskList;
    }

    @Override
    public Task findById(Long id) throws NoSuchElementException {
        Task findTask = taskList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 아이디의 할일이 없습니다."));

        return findTask;
    }

    @Override
    public void delete(Task task) {
        taskList.remove(task);
    }

    @Override
    public Task update(Task task) {
        Task findedTask = findById(task.getId());
        changeTask(findedTask, task.getTitle());
        return findedTask;
    }
    
    private void changeTask(Task task, String title) {
        task.setTitle(title);
    }


}
