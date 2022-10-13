package com.codesoom.assignment.service;

import com.codesoom.assignment.models.RequestTaskDTO;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private static final TaskService taskService = new TaskService();
    private static final List<Task> taskDataList = new ArrayList<>();

    private TaskService() {
    }

    public static TaskService getInstance() {
        return taskService;
    }

    public List<Task> getTasks() {
        return taskDataList;
    }

    public Optional<Task> getTaskByUserId(Long userId) {
        return taskDataList.stream()
                .filter(t -> t.getId().equals(userId))
                .findFirst();
    }

    public Task createTask(RequestTaskDTO.Create request) {
        Task task = request.toEntity();
        task.setId(TaskId.getNewId());
        taskDataList.add(task);

        return task;
    }

    public Task updateTask(Task originTask, RequestTaskDTO.Update request) {
        request.setId(originTask.getId());
        Task task = request.toEntity();
        int indexOfOriginTask = taskDataList.indexOf(originTask);
        taskDataList.set(indexOfOriginTask, task);

        return task;
    }

    public boolean deleteTask(Task task) {
        return taskDataList.remove(task);
    }
}
