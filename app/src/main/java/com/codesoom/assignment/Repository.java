package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    List<Task> tasks = new ArrayList<>();
    Long id = 1L;

    public List<Task> findAll() {
        return tasks;
    }

    public Task findById(Long idValue) {
        Task getTask = null;
        for(Task task : tasks) {
            if(task.getId() == idValue){
                getTask = task;
                break;
            }
        }
        return getTask;
    }

    public void create(Task postTask) {
        postTask.setId(id++);
        tasks.add(postTask);
    }

    public void update(Task updateTask, String title) {
        updateTask.setTitle(title);
    }

    public void remove(Task deleteTask) {
        tasks.remove(deleteTask);
    }
}
