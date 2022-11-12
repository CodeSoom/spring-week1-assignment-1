package com.codesoom.assignment.handler;

import com.codesoom.assignment.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TaskHttpHandlerTest {

    static final List<Task> tasks = new ArrayList<>();

    @BeforeEach
    void before() {
        Task task = new Task();
        task.setTitle("task1");

        Task task2 = new Task();
        task2.setTitle("task2");

        Task task3 = new Task();
        task3.setTitle("task3");

        tasks.add(task);
        tasks.add(task2);
        tasks.add(task3);
    }
    
    @Test
    void getFilteredTask() {
        String taskId = "2";
        int id = Integer.parseInt(taskId);

        Optional<Task> task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst();

        Assertions.assertEquals(task.get().getTitle(), "task2");
    }

    @Test
    void removeTask() {
        String taskId = "1";
        tasks.remove(Integer.parseInt(taskId)-1);

        Assertions.assertThrows(IndexOutOfBoundsException.class,
                () -> {
            tasks.remove(Integer.parseInt("3")-1);
                });
    }
    
    @Test
    void isNumeric(){
        String[] pathSegments = new String[]{"1", "12xx"};
        for (int i=0; i<pathSegments.length; i++) {
            System.out.println("isNumeric(pathSegments[i]) = " + isNumeric(pathSegments[i]));
        }
    }

    private boolean isNumeric(String lastSegment) {
        return lastSegment != null && lastSegment.matches("[0-9.]+");
    }
}
