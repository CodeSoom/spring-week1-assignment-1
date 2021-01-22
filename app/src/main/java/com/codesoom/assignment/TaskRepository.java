package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {

    private static Map<Long, Task> taskStore = new HashMap<>();
    private static long sequence = 1L;

    public static Map<Long, Task> getTaskStore() {
        return taskStore;
    }

    public Task createNewTask(Task newTask) {
        newTask.setId(sequence);
        sequence = sequence + 1;
        newTask.setTitle(newTask.getTitle());
        taskStore.put(newTask.getId(), newTask);
        System.out.println("새로운 Task 추가 완료 : " + "id=" + newTask.getId() + "/ title=" + newTask.getTitle());
        return newTask;
    }

    public void deleteTask(Long deleteId) {
        taskStore.remove(deleteId);
        System.out.println("Task 삭제 완료");
    }

    public void updateTaskTitle(Long idForUpdateTask, Task updateTaks) {
        taskStore.replace(idForUpdateTask, updateTaks);
        System.out.println("Task 수정 완료");
    }
}
