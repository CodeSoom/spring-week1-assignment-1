package com.codesoom.assignment.tasks;

import com.codesoom.assignment.models.Task;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskSerializerTest {
    @Test
    void testTasksToJson() {
        ArrayList<Task> tasks = new ArrayList<>();
        TaskSerializer taskSerializer = new TaskSerializer();
        String json = taskSerializer.tasksToJson(tasks);
        assertEquals(json, "[]");

        Task task = new Task();
        task.setId(1);
        task.setTitle("과제");
        tasks.add(task);
        json = taskSerializer.tasksToJson(tasks);
        assertNotEquals(json, "[]");
        assertEquals(json, "[ {\n" +
                "  \"id\" : 1,\n" +
                "  \"title\" : \"과제\"\n" +
                "} ]");
    }

    @Test
    void testTaskToJson() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("과제");
        TaskSerializer taskSerializer = new TaskSerializer();
        String json = taskSerializer.taskToJson(task);
        assertEquals(json, "{\n" +
                "  \"id\" : 1,\n" +
                "  \"title\" : \"과제\"\n" +
                "}");
    }

    @Test
    void jsonToTask() {
        int id = 1;
        String title = "과제하기";
        String json = "{\n" +
                "  \"id\" : " + id + ",\n" +
                "  \"title\" : \"" + title + "\"\n" +
                "}";
        TaskSerializer taskSerializer = new TaskSerializer();
        Task task = taskSerializer.jsonToTask(json);
        assertEquals(id, task.getId());
        assertEquals(title, task.getTitle());
    }
}