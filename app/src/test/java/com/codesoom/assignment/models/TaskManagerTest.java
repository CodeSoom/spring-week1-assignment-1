package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskManagerTest {
    @Test
    void taskManagerTest() throws Exception {
        TaskManager.insert("sample");
        Task task = TaskManager.find(1);
        assertEquals("sample", task.title());

        TaskManager.insert("sample2");
        List<Task> tasks = TaskManager.find();
        assertEquals(2, tasks.size());

        Task task2 = TaskManager.find(2);
        assertEquals("sample2", task2.title());

        try {
            TaskManager.insert(new Task(1, "sample"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("id 1 is already exists", e.getMessage());
        }

        try {
            TaskManager.modify(new Task(3, "not exist"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }
        Task notExistTask = TaskManager.find(3);
        assertNull(notExistTask);

        TaskManager.modify(new Task(1, "sample1"));
        Task task1 = TaskManager.find(1);
        assertEquals("sample1", task1.title());

        try {
            TaskManager.delete(3);
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }

        TaskManager.delete(1);
        Task deletedTask = TaskManager.find(1);
        assertNull(deletedTask);

        List<Task> finalTasks = TaskManager.find();
        assertEquals(1, finalTasks.size());
    }
}
