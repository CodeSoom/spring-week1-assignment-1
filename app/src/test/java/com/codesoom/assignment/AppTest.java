package com.codesoom.assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.codesoom.assignment.services.TaskService;
import org.junit.jupiter.api.BeforeAll;

class AppTest {
    private TaskService taskService = new TaskService();

    @BeforeAll
    void setup() {
        this.taskService.register("task 1");
        this.taskService.register("task 2");
    }

}
