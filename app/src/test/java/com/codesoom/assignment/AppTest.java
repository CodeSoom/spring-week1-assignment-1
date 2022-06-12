package com.codesoom.assignment;

import com.codesoom.assignment.services.TaskManager;
import org.junit.jupiter.api.BeforeAll;

class AppTest {
    private TaskManager taskManager = new TaskManager();

    @BeforeAll
    void setup() {
        this.taskManager.register("task 1");
        this.taskManager.register("task 2");
    }

}
