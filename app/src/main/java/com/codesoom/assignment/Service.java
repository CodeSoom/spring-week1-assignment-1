package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.List;

public class Service {
    Long id = 1L;

    public Task getTask(Long idValue, List<Task> tasks) {
        Task getTask = null;
        for(Task task : tasks) {
            if(task.getId() == idValue){
                getTask = task;
                break;
            }
        }
        return getTask;
    }

    public void createTask(Task postTask, List<Task> tasks) {
        postTask.setId(id++);
        tasks.add(postTask);
    }

    public void updateTask(Task updateTask, String title) {
        updateTask.setTitle(title);
    }

    public void deleteTask(Task deleteTask, List<Task> tasks) {
        tasks.remove(deleteTask);
    }
}
