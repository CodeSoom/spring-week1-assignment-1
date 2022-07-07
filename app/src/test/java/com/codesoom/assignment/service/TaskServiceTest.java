package com.codesoom.assignment.service;

import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    final TaskService taskService = new TaskService();

    @Test
    void createTaskTest() {
        Task taskToEquals = new Task(0L, "BJP");
        assertEquals(taskToEquals, taskService.createTask("BJP"));
    }
}