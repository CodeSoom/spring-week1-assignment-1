package com.codesoom.assignment.models;


import com.codesoom.assignment.errors.AlreadyExistsIdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TaskManagerTest {
    @Test
    void insertAlreadyExistsID() throws ClassNotFoundException {
        Task task = new Task(1L, "test");
        TaskManager.insert(task);
        try {
            TaskManager.insert(task);
        } catch (AlreadyExistsIdException ignored) {
            return;
        }
        fail();
    }
}
