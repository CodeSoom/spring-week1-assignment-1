package com.codesoom.assignment.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskManagerTest {
    @Test
    void taskManagerTest() throws Exception {
        Task task = TaskManager.insert("sample");
        assertEquals("sample", task.title());

        TaskManager.insert(new Task(2, "sample2"));
        List<Task> tasks = TaskManager.find();
        assertEquals(2, tasks.size());

        Task task2 = TaskManager.find(2);
        assertEquals("sample2", task2.title());

        Task task3 = TaskManager.insert("sample3");
        assertEquals(3, task3.id());

        try {
            TaskManager.insert(new Task(1, "sample"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("id 1 is already exists", e.getMessage());
        }

        try {
            TaskManager.modify(new Task(100, "not exist"));
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }
        Task notExistTask = TaskManager.find(100);
        assertNull(notExistTask);

        TaskManager.modify(new Task(1, "sample1"));
        Task task1 = TaskManager.find(1);
        assertEquals("sample1", task1.title());

        try {
            TaskManager.delete(100);
            throw new Exception("failed test");
        } catch (Exception e) {
            assertEquals("not exist task id", e.getMessage());
        }

        TaskManager.delete(1);
        Task deletedTask = TaskManager.find(1);
        assertNull(deletedTask);

        List<Task> finalTasks = TaskManager.find();
        assertEquals(2, finalTasks.size());
    }
}
