package com.codesoom.assignment.domain;

import org.junit.jupiter.api.Test;

class TaskTest {


    @Test
    void createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("test");

        System.out.println(task.toString());
    }

}