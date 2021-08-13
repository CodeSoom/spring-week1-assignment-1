package com.codesoom.assignment;

import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.codesoom.assignment.models.Task;
import java.io.IOException;

public class TaskManager {

    private static final TaskManager uniqueInstance = new TaskManager();

    private final TaskMapper taskMapper = new TaskMapper();
    private final TaskFactory taskFactory = new TaskFactory();
    private final TaskMap taskMap = new TaskMap();

    private Long lastId = 0L;

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return uniqueInstance;
    }

    public String getAllTasks() throws IOException {
        return taskMapper.toJson(taskMap);
    }

    public String findTaskWith(long id) throws IOException {
        Task task = findTask(id);
        return taskMapper.toJsonWith(task);
    }

    public String createTask(String title) throws IOException {
        Task task = taskFactory.toTask(title);

        lastId++;
        task.setId(lastId);

        taskMap.put(lastId, task);

        return taskMapper.toJsonWith(task);
    }

    public String updateTask(long id, String title) throws IOException {
        Task task = findTask(id);
        Task content = taskFactory.toTask(title);

        String newTitle = content.getTitle();
        task.setTitle(newTitle);

        return taskMapper.toJsonWith(task);
    }

    public void deleteTask(long id) {
        Task task = findTask(id);
        Long taskId = task.getId();

        taskMap.remove(taskId);
    }

    private Task findTask(long id) {
        Task task = taskMap.get(id);
        if (task == null) {
            throw new TaskIdNotFoundException();
        }

        return task;
    }
}
