package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskJsonTransferTest {
    TaskJsonTransfer transfer = new TaskJsonTransfer();

    @Test
    void transferJsonToTask() {
        String jsonString = "{\"title\": \"Play Game\"}";
        assertDoesNotThrow(() -> {
            Task task = transfer.jsonStringToTask(jsonString);

            assertNotNull(task);
            assertEquals("Play Game", task.getTitle());
        });
    }

    @Test
    void transferTaskToJson() {
        String expectJsonString = "{\"id\":1,\"title\":\"Play Game\"}";
        Task task = new Task(1L, "Play Game");

        assertDoesNotThrow(() -> {
            String json = transfer.taskToJson(task);

            assertEquals(expectJsonString, json);
        });
    }

    @Test
    void transferTaskListToJsonArray() {
        String expectJsonString = "[{\"id\":1,\"title\":\"Play Game\"}]";

        List<Task> taskList = new ArrayList<>();
        Task task = new Task(1L, "Play Game");
        taskList.add(task);

        assertDoesNotThrow(() -> {
            String json = transfer.taskListToJson(taskList);

            assertEquals(expectJsonString, json);
        });

    }
}
