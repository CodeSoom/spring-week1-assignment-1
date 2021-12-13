package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TaskManager {
    List<Task> tasks = new ArrayList<>();

    public Long insertTask(Task task){
        long id = tasks.size() == 0 ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
        task.setId(id);
        tasks.add(task);
        return id;
    }

    public void deleteTask(Long id){
        Task deleteTask = tasks.stream().filter(task -> task.getId() == id).findFirst().get();
        tasks.remove(deleteTask);
    }
    public Task readTask(Long id){
        return tasks.stream().filter(task -> task.getId() == id).findFirst().get();
    }

    public void updateTask(Long id, Task task){
        task.setId(id);
        int index = IntStream.range(0,tasks.size())
                .filter(i -> tasks.get(i).getId() == id).findFirst().getAsInt();
        tasks.set(index, task);
    }

    public List<Task> getTasks(){
        return tasks;
    }
}
